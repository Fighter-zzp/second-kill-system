package com.zzp.second.kill.commons.dto.code;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 佐斯特勒
 * <p>
 * 手机验证码
 * </p>
 * @version v1.0.0
 * @date 2020/1/17 14:39
 * @see SmsCode
 **/
@Getter@Setter
@NoArgsConstructor
public class SmsCode {
    /**
     * 手机验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    public SmsCode(String code, Long et) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(et);
    }

    /**
     * 判断手机验证码是否过期
     * @return Boolean
     */
    public boolean isExpire(){
        return LocalDateTime.now().isAfter(this.expireTime);
    }
}
