package gamingcafe;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeGenerator {

    public static void saveQRCodeAsFile(String pcId, String pcName) {
        try {
            // Project eka thiyena main folder eke path eka auto gannawa
            String projectPath = System.getProperty("user.dir");
            String pcdownloadsPath = System.getProperty("user.home");
            
            // E path eka athule kiyala folder eka hadanawa
            String folderPath = projectPath + "\\Downloads\\\\GamingCafe_QRs\\"; 
            String folderPath1 = pcdownloadsPath + "\\Downloads\\"; 
            
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String qrData = "PC ID: " + pcId + "PC ID: "+ pcName ;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 250, 250);

            Path path = Paths.get(folderPath + pcName + ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            
            System.out.println("QR saved to: " + path.toString());

        } catch (Exception e) {
            System.out.println("QR Error: " + e.getMessage());
        }
    }

}
