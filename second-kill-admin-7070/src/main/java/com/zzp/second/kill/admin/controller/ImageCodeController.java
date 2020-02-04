package com.zzp.second.kill.admin.controller;

import com.zzp.second.kill.commons.dto.code.ImageCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ZZP
 */
@RestController
@RequestMapping("/imageCode")
public class ImageCodeController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping
    public Object getImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var imageCode = generate();
        // 将图片验证码存放到redis中，并设置60s过期
        var imageCodeKey = UUID.randomUUID().toString();
        //redis中存储的imageCodeKey
        redisTemplate.opsForValue().set(imageCodeKey, imageCode.getCode(), 60, TimeUnit.SECONDS);

        // 在前后端分离的场景下，图片以base64的形式返回
        //io流
        var baos = new ByteArrayOutputStream();
        //写入流中
        ImageIO.write(imageCode.getBufferedImage(), "JPEG", baos);

        //转换成字节
        var bytes = baos.toByteArray();

        var encoder = Base64.getEncoder();
        //转换成base64串
        var png_base64 =  encoder.encodeToString(bytes).trim();
        //删除 \r\n
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");

        var map = new HashMap<String, Object>();
        map.put("code", 1);
        map.put("data", png_base64);
        map.put("imageCodeKey", imageCodeKey);

        return map;
    }

    /**
     * 生成图片验证码
     * @return 图片验证码类
     */
    private ImageCode generate() {
        var width = 67;
        var height = 30;
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        var g = image.getGraphics();
        var random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            var x = random.nextInt(width);
            var y = random.nextInt(height);
            var xl = random.nextInt(12);
            var yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 随机生成文字
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            var rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 22);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), 60);
    }

    /**
     * 生成随机背景条纹
     * @param fc .
     * @param bc .
     * @return .
     */
    private Color getRandColor(int fc, int bc) {
        var random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        var r = fc + random.nextInt(bc - fc);
        var g = fc + random.nextInt(bc - fc);
        var b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}

