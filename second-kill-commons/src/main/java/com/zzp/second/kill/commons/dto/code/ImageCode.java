package com.zzp.second.kill.commons.dto.code;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * @author 佐斯特勒
 * <p>
 *  图片编码
 * </p>
 * @version v1.0.0
 * @date 2020/1/17 14:36
 * @see  ImageCode
 **/
@Setter@Getter
@NoArgsConstructor
public class ImageCode extends SmsCode{
    /**
     * 图片流
     */
    private BufferedImage bufferedImage;

    public ImageCode(BufferedImage bufferedImage, String code, long et) {
        super(code, et);
        this.bufferedImage = bufferedImage;
    }

}
