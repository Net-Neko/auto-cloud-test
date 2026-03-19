package org.zzf.controller.common;

import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zzf.util.JsonData;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 詹泽峰
 * @date 2025/11/22 18:05
 */
@Controller
public class TestController {

    private static Set<String> TOKEN_SET = new HashSet<>();

    @GetMapping("/api/v2/test/detail")
    @ResponseBody
    public JsonData detail(Long id) {
        return JsonData.buildSuccess("传入id=" + id);
    }

    /**
     * 模拟form表单登录
     *
     * @param mail
     * @param pwd
     * @return
     */
    @RequestMapping("/api/v1/test/login_form")
    @ResponseBody
    public JsonData login(String mail, String pwd) {
        if (mail.startsWith("a")) {
            return JsonData.buildError("账号错误");
        }
        return JsonData.buildSuccess("mail=" + mail + ",pwd=" + pwd);
    }

    /**
     * json提交
     *
     * @param map
     * @return org.zzf.controller.util.JsonData
     * @author: 詹泽峰
     * @date: 2025/11/23 12:41
     */
    @PostMapping("/api/v1/test/pay_json")
    @ResponseBody
    public JsonData pay(@RequestBody Map<String, String> map) {

        String id = map.get("id");
        String amount = map.get("amount");
        return JsonData.buildSuccess("id=" + id + ",amount=" + amount);
    }

    /**
     * json提交, 加上随机睡眠时间
     *
     * @param map
     * @return org.zzf.controller.util.JsonData
     * @author: 詹泽峰
     * @date: 2025/11/23 12:41
     */
    @PostMapping("/api/v1/test/pay_json_sleep")
    @ResponseBody
    public JsonData paySleep(@RequestBody Map<String, String> map) {

        try {
            int value = RandomUtil.randomInt(1000);
            TimeUnit.MICROSECONDS.sleep(value);
            String id = map.get("id");
            String amount = map.get("amount");
            return JsonData.buildSuccess("id=" + id + ",amount=" + amount + ",sleep=" + value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * get方式提交
     *
     * @param id
     * @return org.zzf.controller.util.JsonData
     * @author: 詹泽峰
     * @date: 2025/11/23 12:41
     */
    @GetMapping("/api/v1/test/query")
    @ResponseBody
    public JsonData queryDetail(Long id) {
        return JsonData.buildSuccess("id=" + id);
    }

    /**
     * get方式，随机睡眠时间
     *
     * @param id
     * @return org.zzf.controller.util.JsonData
     * @author: 詹泽峰
     * @date: 2025/11/23 12:42
     */
    @GetMapping("/api/v1/test/query_sleep")
    @ResponseBody
    public JsonData querySleep(Long id) {
        try {
            int value = RandomUtil.randomInt(1000);
            TimeUnit.MICROSECONDS.sleep(value);
            return JsonData.buildSuccess("id=" + id + ",sleep=" + value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * get方式，id取模3是0后则http状态码500
     *
     * @param id
     * @param response
     * @return org.zzf.controller.util.JsonData
     * @author: 詹泽峰
     * @date: 2025/11/23 12:42
     */
    @GetMapping("/api/v1/test/query_error_code")
    @ResponseBody
    public JsonData queryError(Long id, HttpServletResponse response) {

        if (id % 3 == 0) {
            response.setStatus(500);
        }
        return JsonData.buildSuccess("id=" + id);
    }
}
