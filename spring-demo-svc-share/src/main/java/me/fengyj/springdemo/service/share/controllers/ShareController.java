package me.fengyj.springdemo.service.share.controllers;

import me.fengyj.springdemo.models.Price;
import me.fengyj.springdemo.models.ShareClassOperation;
import me.fengyj.springdemo.service.share.ShareService;
import me.fengyj.springdemo.service.share.service.PriceService;
import me.fengyj.springdemo.service.share.service.ShareClassOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/share", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class ShareController implements ShareService {

    private final ShareClassOperationService shareSvc;
    private final PriceService priceSvc;

    public ShareController(
            @Autowired ShareClassOperationService shareSvc,
            @Autowired PriceService priceSvc
    ) {
        this.shareSvc = shareSvc;
        this.priceSvc = priceSvc;
    }

    @Override
    public List<ShareClassOperation> getList(String symbolOrName) {
        return shareSvc.getList(symbolOrName);
    }

    @Override
    public ShareClassOperation getShare(String shareClassId) {

        return shareSvc.get(shareClassId);
    }

    @Override
    public ShareClassOperation updateShare(String shareClassId, ShareClassOperation share) {

        return shareSvc.update(share);
    }

    @Override
    public ShareClassOperation addShare(ShareClassOperation share) {

        return shareSvc.add(share);
    }

    @Override
    public boolean deleteShare(String shareClassId) {

        return shareSvc.delete(shareClassId);
    }

    @Override
    public List<Price> getPriceList(String shareClassId) {

        return priceSvc.getHistory(shareClassId);
    }

    @Override
    public List<Price> refreshPriceList(String shareClassId) {

        return priceSvc.reloadAndUpdate(shareClassId);
    }
}
