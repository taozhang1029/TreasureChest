package com.kingsley.treasure.qrcode;

/**
 * @author kingsley
 * @time 2022/2/9 20:39
 * @ide IntelliJ IDEA
 * @name com.kingsley.treasure.qrcode.QRPicFormat
 * @desc 二维码图片格式枚举
 */
public enum QRPicFormat {
    /**
     * png格式
     */
    PNG("png"),
    
    /**
     * jpg格式
     */
    JPG("jpg");
    
    public String format;
    
    QRPicFormat(String format) {
        this.format = format;
    }
}
