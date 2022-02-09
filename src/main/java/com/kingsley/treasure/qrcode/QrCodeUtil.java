package com.kingsley.treasure.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Hashtable;

public class QrCodeUtil {
    
    /**
     * 二维码默认尺寸
     */
    public static final int DEFAULT_QRCODE_SIZE = 300;
    
    /**
     * 生成二维码 将二维码图片写到指定文件目录
     *
     * @param content 二维码内容
     */
    public static void generateQRCode(String content) {
        generateQRCode(content, DEFAULT_QRCODE_SIZE);
    }
    
    /**
     * 生成二维码 将二维码图片写到指定文件目录
     *
     * @param content 二维码内容
     * @param size    二维码图片尺寸
     */
    public static void generateQRCode(String content, int size) {
        generateQRCode(content, size, "qr_" + System.currentTimeMillis());
    }
    
    /**
     * 生成二维码 将二维码图片写到指定文件目录
     *
     * @param content 二维码内容
     * @param size    二维码图片尺寸
     * @param picName 二维码图片名称
     */
    public static void generateQRCode(String content, int size, String picName) {
        generateQRCode(content, size, size, picName, null);
    }
    
    /**
     * 生成二维码 将二维码图片写到指定文件目录
     *
     * @param content   二维码内容
     * @param width     二维码宽度
     * @param height    二维码高度，通常建议二维码宽度和高度相同
     * @param picName   二维码图片名称
     * @param picFormat 二维码图片格式，jpg/png
     */
    public static void generateQRCode(String content, int width, int height, String picName, QRPicFormat picFormat) {
        boolean flag = true;
        if (picFormat == null) {
            picFormat = QRPicFormat.JPG;
            flag = false;
        }
        if (picName.endsWith(QRPicFormat.PNG.format) || picName.endsWith(QRPicFormat.JPG.format)) {
            if (flag) {
                System.out.println("图片格式将以picFormat参数为准！");
            }
            picName = picName.substring(0, picName.length() - 3);
        }
        String fullPath = picName + "." + picFormat.format;
        File file = new File(fullPath);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            // 构造二维字节矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = file.toPath();
            // 将二位字节矩阵按照指定图片格式，写入指定文件目录，生成二维码图片
            MatrixToImageWriter.writeToPath(bitMatrix, picFormat.format, path);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 生成二维码 生成二维码图片字节流
     *
     * @param content   二维码内容
     * @param size      二维码宽度和高度
     * @param picFormat 二维码图片格式
     */
    public static byte[] generateQRCodeByte(String content, int size, QRPicFormat picFormat) {
        byte[] codeBytes = null;
        try {
            // 构造二维字节矩阵，将二位字节矩阵渲染为二维缓存图片
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size);
            BufferedImage image = toBufferedImage(bitMatrix);
            
            // 定义输出流，将二维缓存图片写到指定输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, picFormat.format, out);
            
            // 将输出流转换为字节数组
            codeBytes = out.toByteArray();
            
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return codeBytes;
    }
    
    /**
     * 将二维字节矩阵渲染为二维缓存图片
     *
     * @param matrix 二维字节矩阵
     * @return 二维缓存图片
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int onColor = 0xFF000000;
        int offColor = 0xFFFFFFFF;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
            }
        }
        return image;
    }
    
    /**
     * 解析二维码内容
     *
     * @param filepath 二维码路径
     */
    public static QrCodeParserResult parseQRCode(String filepath) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        File file = new File(filepath);
        try {
            // 图片缓冲
            BufferedImage image = ImageIO.read(file);
            // 二进制比特图
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            // 二维码结果
            Result result = multiFormatReader.decode(binaryBitmap);
            // System.out.println("读取二维码： " + result.toString());
            // System.out.println("二维码格式： " + result.getBarcodeFormat());
            // System.out.println("二维码内容： " + result.getText());
            return QrCodeParserResult.builder().barcodeFormat(result.getBarcodeFormat()).text(result.getText()).build();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * main 测试方法
     */
    public static void main(String[] args) {
        
        // 生成二维码，直接写到本地
        generateQRCode("测试生成二维码", 300, "/Users/kingsley/IdeaProjects/TreasureChest/src/main/resources/qrcode.jpg");
        
        QrCodeParserResult result = parseQRCode("/Users/kingsley/IdeaProjects/TreasureChest/src/main/resources/qrcode.jpg");
        System.out.println(result);
    }
}