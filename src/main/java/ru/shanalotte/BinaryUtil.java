package ru.shanalotte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BinaryUtil {
     private static final Logger logger = LoggerFactory.getLogger(BinaryUtil.class);
    public static double binaryStringToDouble(String myBinStr)
    {
        double myDbl = 0.0;
        String myRegex = "[01]*";
        if(myBinStr == null || myBinStr.equals("") ||  myBinStr.length() > 64 || !myBinStr.matches(myRegex))
        {
            // throw an error
        }
        if (myBinStr.length() == 64)
        {
            if (myBinStr.charAt(0) == '1')
            {
                String negBinStr = myBinStr.substring(1);
                myDbl = -1 * Double.longBitsToDouble(Long.parseLong(negBinStr, 2));
            }
            else if (myBinStr.charAt(0) == '0')
            {
                myDbl = Double.longBitsToDouble(Long.parseLong(myBinStr, 2));
            }
        }
        else if(myBinStr.length() < 64)
        {
            myDbl = Double.longBitsToDouble(Long.parseLong(myBinStr, 2));
        }
        logger.debug("Значение = {}", myDbl);
        return myDbl;
    }

    public static String convertStringToBinary(String input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))
                            .replaceAll(" ", "0")
            );
        }
        return result.toString();

    }

    public static String binaryToText(String binary) {
        if (binary.equals("")) return "";
        return Arrays.stream(binary.split("(?<=\\G.{8})"))/* regex to split the bits array by 8*/
                .parallel()
                .map(eightBits -> (char)Integer.parseInt(eightBits, 2))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }
}
