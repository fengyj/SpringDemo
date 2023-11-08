package me.fengyj.springdemo.service.share;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.fengyj.springdemo.models.Price;
import me.fengyj.springdemo.models.ShareClassOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Share information service APIs", description = "Provide the APIs to get share and price information.")
public interface ShareService {

    @GetMapping(value = "/list")
    @Operation(summary = "Get share list")
    List<ShareClassOperation> getList(
            @RequestParam(name = "q", required = false) @Parameter(description = "search by symbol or name.") String symbolOrName);

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get share by key")
    ShareClassOperation getShare(
            @PathVariable(name = "id") @Parameter(description = "ShareClassId", required = true) String shareClassId);

    @PostMapping(value = "/{id}")
    @Operation(summary = "Update share")
    ShareClassOperation updateShare(
            @PathVariable(name = "id") @Parameter(description = "ShareClassId", required = true) String shareClassId,
            @RequestBody ShareClassOperation share);

    @PostMapping(value = "/new")
    @Operation(summary = "Add new share")
    ShareClassOperation addShare(
            @RequestBody ShareClassOperation share);

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete share by key")
    boolean deleteShare(
            @PathVariable(name = "id") @Parameter(description = "ShareClassId", required = true) String shareClassId);

    @GetMapping("/{id}/prices")
    @Operation(summary = "Get share price list")
    List<Price> getPriceList(
            @PathVariable(name = "id") @Parameter(description = "ShareClassId", required = true) String shareClassId);

    @GetMapping("/{id}/refresh-prices")
    @Operation(summary = "Reload the price data from external source")
    List<Price> refreshPriceList(
            @PathVariable(name = "id") @Parameter(description = "ShareClassId", required = true) String shareClassId);
}
