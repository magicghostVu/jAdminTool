package utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by CPU10340_LOCAL on 24/05/2017.
 */
public class ByteUtils {
    private static final int HEX_BYTES_PER_LINE = 16;
    private static final char TAB = 9;
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final char DOT = 46;

    private static Charset charset = Charset.forName("UTF-8");

    public ByteUtils() {
    }

    public static byte[] resizeByteArray(byte source[], int pos, int size) {
        byte tmpArray[] = new byte[size];
        System.arraycopy(source, pos, tmpArray, 0, size);
        return tmpArray;
    }

    public static String fullHexDump(ByteBuffer buffer, int bytesPerLine) {
        return fullHexDump(buffer.array(), bytesPerLine);
    }

    public static String fullHexDump(ByteBuffer buffer) {
        return fullHexDump(buffer.array(), 16);
    }

    public static String fullHexDump(byte buffer[]) {
        return fullHexDump(buffer, 16);
    }

    public static String fullHexDump(byte buffer[], int bytesPerLine) {
        StringBuilder sb = (new StringBuilder("Binary size: ")).append(buffer.length).append("\n");
        StringBuilder hexLine = new StringBuilder();
        StringBuilder chrLine = new StringBuilder();
        int index = 0;
        int count = 0;
        do {
            byte currByte = buffer[index];
            String hexByte = Integer.toHexString(currByte & 0xff);
            if (hexByte.length() == 1)
                hexLine.append("0");
            hexLine.append(hexByte.toUpperCase()).append(" ");
            char currChar = currByte < 33 || currByte > 126 ? '.' : (char) currByte;
            chrLine.append(currChar);
            if (++count == bytesPerLine) {
                count = 0;
                sb.append(hexLine).append('\t').append(chrLine).append(NEW_LINE);
                hexLine.delete(0, hexLine.length());
                chrLine.delete(0, chrLine.length());
            }
        } while (++index < buffer.length);
        if (count != 0) {
            for (int j = bytesPerLine - count; j > 0; j--) {
                hexLine.append("   ");
                chrLine.append(" ");
            }

            sb.append(hexLine).append('\t').append(chrLine).append(NEW_LINE);
        }
        return sb.toString();
    }
    public static String readString(ByteBuffer bf){
        String data= "";
        try{
            short lengthString= bf.getShort();
            byte[] arr= new byte[lengthString];
            bf.get(arr);
            data= toString(arr);

        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static String toString(byte[] arr){
        int lenght= arr.length;
        for (int i = 0; i < arr.length-1 ; i++) {
            if(arr[i]==0 && arr[i+1]==0){
                lenght=i;
                break;
            }
        }
        return new String(arr, 0, lenght, charset);
    }

}
