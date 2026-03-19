package org.zzf.util;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

import java.nio.charset.StandardCharsets;

/**
 * 文件工具类
 *
 * @author 詹泽峰
 * @date 2026/02/24 22:50
 */
public class CustomFileUtil {

    /**
     * 文件名生成
     *
     * @author: 詹泽峰
     * @date: 2026/2/24 22:51
     * @param filename 文件名
     * @return java.lang.String 添加时间戳和UUID的文件名
     */
    public static String getFilename(String filename) {
        return System.currentTimeMillis() + "-" + IdUtil.fastUUID() + "-" + filename;
    }

    /**
     * 通过URL读取远程文本内容（用Hutool HttpUtil完全替代手写流操作）
     *
     * @author: 詹泽峰
     * @date: 2026/2/25 17:36
     * @param urlStr 远程文件URL
     * @return java.lang.String 远程文本内容
     */
    public static String readRemoteFile(String urlStr) {
        try {
            return HttpUtil.get(urlStr);
        } catch (Exception e) {
            throw new RuntimeException("远程文件读取异常，URL：" + urlStr, e);
        }
    }

    /**
     * 获取文件后缀
     *
     * @author: 詹泽峰
     * @date: 2026/2/25 18:07
     * @param remoteFilePath 文件路径/URL
     * @return java.lang.String 带点的文件后缀（如.txt），无后缀返回空字符串
     */
    public static String getSuffix(String remoteFilePath) {
        // Hutool的FileUtil.extName() 获取无点的后缀，拼接点即可；自动处理null/空字符串/无后缀场景
        String ext = FileUtil.extName(remoteFilePath);
        return ext.isEmpty() ? "" : "." + ext;
    }
}
