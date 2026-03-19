package org.zzf.stress;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Runtime调用测试
 *
 * @author 詹泽峰
 * @date 2025/11/25 12:11
 */
public class RuntimeTest {
    public static void main(String[] args) {
        try {
            // 双引号内填写命令
            Process process = Runtime.getRuntime().exec("");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("line: " + line);
            }
            // 等待执行结束
            int waitedForCode = process.waitFor();
            System.out.println("waitedForCode: " + waitedForCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
