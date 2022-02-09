package com.kingsley.treasure.qrcode;

import com.google.zxing.BarcodeFormat;
import lombok.*;

/**
 * @author kingsley
 * @time 2022/2/9 21:21
 * @ide IntelliJ IDEA
 * @name com.kingsley.treasure.qrcode.QRCodeParserResult
 * @desc 二维码解析结果
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeParserResult {
    
    /**
     * 二维码内容文本
     */
    private String text;
    
    /**
     * 二维码格式
     */
    private BarcodeFormat barcodeFormat;
    
    @Override
    public String toString() {
        return "二维码内容: " + text + "\n二维码格式: " + barcodeFormat;
    }
}
