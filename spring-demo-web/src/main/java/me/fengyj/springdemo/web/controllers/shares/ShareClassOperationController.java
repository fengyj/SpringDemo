package me.fengyj.springdemo.web.controllers.shares;

import me.fengyj.common.exceptions.ApplicationBaseException;
import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.common.utils.StringUtils;
import me.fengyj.springdemo.models.ShareClassOperation;
import me.fengyj.springdemo.service.share.client.ShareServiceClient;
import me.fengyj.springdemo.utils.exceptions.DataIssueException;
import me.fengyj.springdemo.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/shares")
public class ShareClassOperationController {

    private final ShareServiceClient shareSvc;

    public ShareClassOperationController(@Autowired ShareServiceClient shareSvc) {
        this.shareSvc = shareSvc;
    }

    @GetMapping(path = {"", "/", "/list"})
    public String viewShareList(Model model, @RequestParam(name = "q", required = false) String symbolOrName) {
        model.addAttribute("shareList", shareSvc.getList(symbolOrName));
        return "shares/index";
    }

    @GetMapping(path = "/view")
    public String viewShare(Model model, @RequestParam("id") String shareClassId) {

        if (StringUtils.isNullOrWhiteSpace(shareClassId))
            throw new ResourceNotFoundException(ErrorSeverity.Error, ShareClassOperation.class, shareClassId, "The share cannot be " +
                    "found.");

        var share = shareSvc.getShare(shareClassId);

        if (share == null)
            throw new ResourceNotFoundException(ErrorSeverity.Error, ShareClassOperation.class, shareClassId, "The share cannot be " +
                    "found.");

        var prices = shareSvc.getPriceList(shareClassId);
        var pricesDisplayed = prices.stream()
                                    .skip(Math.max(prices.size() - 30, 0))
                                    .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()) * -1)
                                    .toList();

        model.addAttribute("share", share);
        model.addAttribute("priceList", pricesDisplayed);
        return "shares/view";
    }

    @GetMapping(path = "/refresh-price")
    public String refreshPrice(
            Model model,
            @RequestParam("id") String shareClassId,
            @RequestParam(value = "q", required = false) String symbolOrName,
            RedirectAttributes redirectModel) {

        if (StringUtils.isNullOrWhiteSpace(shareClassId))
            throw new ResourceNotFoundException(
                    ErrorSeverity.Error,
                    ShareClassOperation.class,
                    shareClassId,
                    "The share cannot be found.");

        var prices = shareSvc.refreshPriceList(shareClassId);

        redirectModel.addAttribute("id", shareClassId);
        if (!StringUtils.isNullOrWhiteSpace(symbolOrName))
            redirectModel.addAttribute("q", symbolOrName);
        return "redirect:/shares/view";
    }

    @GetMapping(path = "/edit")
    public String editShare(
            Model model,
            @RequestParam(name = "id", required = false) String shareClassId) {

        ShareClassOperation share = null;
        if (shareClassId == null) {
            share = new ShareClassOperation();
        } else {
            share = shareSvc.getShare(shareClassId);
        }
        if (share == null)
            throw new ResourceNotFoundException(
                    ErrorSeverity.Error,
                    ShareClassOperation.class,
                    shareClassId,
                    "The share cannot be found.");

        model.addAttribute("share", share);
        return "shares/edit";
    }

    @PostMapping(path = "/edit")
    public String updateShare(
            Model model,
            @RequestParam(name = "id", required = false) String shareClassId,
            @RequestParam(value = "q", required = false) String symbolOrName,
            @ModelAttribute("share") ShareClassOperation share,
            BindingResult result,
            RedirectAttributes redirectModel) {

        try {
            if (StringUtils.isNullOrWhiteSpace(share.getShareClassId()))
                shareSvc.addShare(share);
            else
                shareSvc.updateShare(share.getShareClassId(), share);
        } catch (DataIssueException ex) {
            result.addError(new FieldError("share", ex.getField(), ex.getMessage()));
        } catch (ApplicationBaseException ex) {
            result.addError(new ObjectError("share", ex.getMessage()));
        }
        if (result.hasErrors())
            return "/shares/edit";

        redirectModel.addAttribute("id", share.getShareClassId());
        if (!StringUtils.isNullOrWhiteSpace(symbolOrName))
            redirectModel.addAttribute("q", symbolOrName);
        return "redirect:/shares/view";
    }

    @DeleteMapping(path = "/delete")
    public String deleteShare(
            @RequestParam(name = "id") String shareClassId,
            @RequestParam(value = "q", required = false) String symbolOrName,
            RedirectAttributes redirectModel) {

        if (!shareSvc.deleteShare(shareClassId))
            throw new ResourceNotFoundException(
                    ErrorSeverity.Warning,
                    ShareClassOperation.class,
                    shareClassId,
                    "Cannot find the share to delete it.");

        redirectModel.addAttribute("id", shareClassId);
        if (!StringUtils.isNullOrWhiteSpace(symbolOrName))
            redirectModel.addAttribute("q", symbolOrName);
        return "redirect:/shares/list";
    }
}
