package me.fengyj.springdemo.service.share.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "spring-demo-share-service")
public interface ShareServiceClient extends me.fengyj.springdemo.service.share.ShareService {
}
