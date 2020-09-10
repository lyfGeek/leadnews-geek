package com.geek.images.controller.v1;

import com.geek.images.apis.ImagesControllerApi;
import com.geek.images.config.InitConfig;
import com.geek.images.service.ICacheImageService;
import com.geek.utils.common.Base64Utils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/api/v1/images")
@Log4j2
public class ImagesController implements ImagesControllerApi {

    @Autowired
    public ICacheImageService cacheImageService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    @RequestMapping(value = "/get", produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage getImage(String u) throws Exception {
        String path = u;
        if (!u.startsWith("http")) {
            path = InitConfig.PREFIX + u;
        }
        log.info("图片访问请求开始#path：{}", path);
        String baseCode = redisTemplate.opsForValue().get(path);
        if (StringUtils.isEmpty(baseCode)) {
            // 从 fastdfs 中获取数据。
            byte[] bytes = cacheImageService.cache2Redis(path, false);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            return bufferedImage;
        }
        byte[] source = Base64Utils.decode(baseCode);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(source));
        log.info("图片访问请求结束#path：{}", path);
        return bufferedImage;
    }

}
