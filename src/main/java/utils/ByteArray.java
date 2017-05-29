package utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by CPU10340_LOCAL on 24/05/2017.
 */
public class ByteArray {
    private byte buffer[];
    private int position;
    private boolean compressed;

    public ByteArray() {
        position = 0;
        compressed = false;
        buffer = new byte[0];
    }

    public ByteArray(byte buf[]) {
        position = 0;
        compressed = false;
        buffer = buf;
    }

    public byte[] getBytes() {
        return buffer;
    }

    public void setBuffer(byte buffer[]) {
        this.buffer = buffer;
        compressed = false;
    }

    public int getLength() {
        return buffer.length;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBytesAvailable() {
        int val = buffer.length - position;
        if (val > buffer.length || val < 0)
            val = 0;
        return val;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public void compress()
            throws Exception {
        if (compressed)
            throw new Exception("Buffer is already compressed");
        try {
            Deflater compressor = new Deflater();
            compressor.setLevel(9);
            compressor.setInput(buffer);
            compressor.finish();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int count;
            for (; !compressor.finished(); bos.write(buf, 0, count))
                count = compressor.deflate(buf);

            bos.close();
            buffer = bos.toByteArray();
            position = 0;
            compressed = true;
        } catch (IOException e) {
            throw new Exception("Error compressing data");
        }
    }

    public void uncompress()
            throws Exception {
        try {
            Inflater decompressor = new Inflater();
            decompressor.setInput(buffer);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int count;
            for (; !decompressor.finished(); bos.write(buf, 0, count))
                count = decompressor.inflate(buf);

            bos.close();
            buffer = bos.toByteArray();
            position = 0;
            compressed = false;
        } catch (DataFormatException e) {
            throw new Exception("Data format exception decompressing buffer");
        } catch (IOException e) {
            throw new Exception("Error decompressing data");
        }
    }

    private void checkCompressedWrite()
            throws Exception {
        if (compressed)
            throw new Exception("Only raw bytes can be written a compressed array. Call Uncompress first.");
        else
            return;
    }

    private void checkCompressedRead()
            throws Exception {
        if (compressed)
            throw new Exception("Only raw bytes can be read from a compressed array.");
        else
            return;
    }

    public byte[] reverseOrder(byte dt[]) {
        return dt;
    }

    public void writeByte(int type) {
        writeByte((byte) type);
    }

    public void writeByte(byte b) {
        byte buf[] = new byte[1];
        buf[0] = b;
        writeBytes(buf);
    }

    public void writeBytes(byte data[]) {
        writeBytes(data, data.length);
    }

    public void writeBytes(byte data[], int count) {
        ByteBuffer newBuffer = ByteBuffer.allocate(count + buffer.length);
        newBuffer.put(buffer);
        byte addBuffer[] = new byte[count];
        ByteBuffer.wrap(data).get(addBuffer, 0, count);
        newBuffer.put(addBuffer);
        buffer = newBuffer.array();
    }

    public void writeBool(boolean b)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeByte(b ? 1 : 0);
            writeBytes(bos.toByteArray());
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeInt(int i)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeInt(i);
            writeBytes(reverseOrder(bos.toByteArray()));
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeShort(short s)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeShort(s);
            writeBytes(reverseOrder(bos.toByteArray()));
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeUShort(int s)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            int b1 = (s & 0xff00) >> 8;
            int b2 = s & 0xff;
            dos.writeByte((byte) b1);
            dos.writeByte((byte) b2);
            writeBytes(bos.toByteArray());
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeLong(long l)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeLong(l);
            writeBytes(reverseOrder(bos.toByteArray()));
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeFloat(float f)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeFloat(f);
            writeBytes(reverseOrder(bos.toByteArray()));
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeDouble(double d)
            throws Exception {
        checkCompressedWrite();
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeDouble(d);
            writeBytes(reverseOrder(bos.toByteArray()));
        } catch (IOException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public void writeUTF(String str)
            throws Exception {
        checkCompressedWrite();
        int utfLen = 0;
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i);
            if (c >= 1 && c <= 127)
                utfLen++;
            else if (c > 2047)
                utfLen += 3;
            else
                utfLen += 2;
        }

        if (utfLen > 32768)
            throw new Exception("String length cannot be greater then 32768 !");
        try {
            writeShort((short) utfLen);
            writeBytes(str.getBytes("UTF8"));
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Error writing to data buffer");
        }
    }

    public byte readByte()
            throws Exception {
        checkCompressedRead();
        return buffer[position++];
    }

    public byte[] readBytes(int count) {
        byte res[] = new byte[count];
        ByteBuffer buf = ByteBuffer.wrap(buffer);
        buf.position(position);
        buf.get(res);
        position += count;
        return res;
    }

    public boolean readBool()
            throws Exception {
        checkCompressedRead();
        return buffer[position++] == 1;
    }

    public int readInt()
            throws Exception {
        checkCompressedRead();
        byte data[] = reverseOrder(readBytes(4));
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);
        try {
            return dis.readInt();
        } catch (IOException e) {
            throw new Exception("Error reading from data buffer");
        }
    }

    public short readShort()
            throws Exception {
        checkCompressedRead();
        byte data[] = reverseOrder(readBytes(2));
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);
        try {
            return dis.readShort();
        } catch (IOException e) {
            throw new Exception("Error reading from data buffer");
        }
    }

    public int readUShort()
            throws Exception {
        checkCompressedRead();
        byte data[] = reverseOrder(readBytes(2));
        int ib1 = (new Integer(data[0])).intValue();
        if (ib1 < 0) {
            ib1 = data[0] & 0x80;
            ib1 += data[0] & 0x7f;
        }
        int ib2 = (new Integer(data[1])).intValue();
        if (ib2 < 0) {
            ib2 = data[1] & 0x80;
            ib2 += data[1] & 0x7f;
        }
        return ib1 * 256 + ib2;
    }

    public long readLong()
            throws Exception {
        checkCompressedRead();
        byte data[] = reverseOrder(readBytes(8));
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);
        try {
            return dis.readLong();
        } catch (IOException e) {
            throw new Exception("Error reading from data buffer");
        }
    }

    public float readFloat()
            throws Exception {
        checkCompressedRead();
        byte data[] = reverseOrder(readBytes(4));
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);
        try {
            return dis.readFloat();
        } catch (IOException e) {
            throw new Exception("Error reading from data buffer");
        }
    }

    public double readDouble()
            throws Exception {
        checkCompressedRead();
        byte data[] = reverseOrder(readBytes(8));
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bis);
        try {
            return dis.readDouble();
        } catch (IOException e) {
            throw new Exception("Error reading from data buffer");
        }
    }

    public String readUTF()
            throws Exception {
        try {
            checkCompressedRead();
            short size = readShort();
            byte data[] = readBytes(size);
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Error reading from data buffer");
        }
    }

    public String getDump() {
        return ByteUtils.fullHexDump(buffer);
    }

    public String getHexDump() {
        if (buffer == null || buffer.length <= 0) return "";
        return ByteUtils.fullHexDump(buffer);
    }
}
