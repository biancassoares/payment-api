package com.soares.payment_api.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrCodeService {

    public String generateQrCode (String content)throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(
                content,
                BarcodeFormat.QR_CODE,
                250,
                250
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(
                bitMatrix,
                "PNG",
                outputStream
        );
        byte[] qrCodeBytes = outputStream.toByteArray();

        String base64QrCode = Base64.getEncoder()
                .encodeToString(qrCodeBytes);

        return base64QrCode;


    }
}
