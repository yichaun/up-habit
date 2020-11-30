package com.up.habit.app.service;

import com.jfinal.kit.Kv;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;
import com.up.habit.Habit;
import com.up.habit.app.controller.render.To;
import com.up.habit.kit.ImageKit;
import com.up.habit.kit.StrKit;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/26 1:23
 */
public class CommonService {

    private static final Log log = Log.getLog("commonService");

    public static CommonService me = new CommonService();

    public static final String CAPTCHA = "_captcha";

    /**
     * TODO:获取验证码
     *
     * @param length
     * @param width
     * @param height
     * @return
     */
    public To captcha(Integer length, Integer width, Integer height) {
        String key = StrKit.getRandomUUID();
        String value = StrKit.getRandomString(false, length == null ? 4 : length);
        try {
            String base64 = ImageKit.captchaBase64(value, width, height);
            Habit.getCache().put(CAPTCHA, key, value.toLowerCase(), 180);
            return To.ok(Kv.by("image", base64).set("uuid", key));
        } catch (Exception e) {
            e.printStackTrace();
            return To.fail("验证码生成失败!");
        }
    }

    /**
     * TODO:图片验证码验证
     *
     * @param key
     * @param value
     * @return boolean
     * @Author 王剑洪 on 2019/12/17 0:58
     **/
    public boolean validateCaptcha(String key, String value) {
        if (StrKit.hasBlank(key, value)) {
            log.error("没有上传uuid和code");
            return false;
        }
        if (value.toLowerCase().equals(Habit.getCache().get(CAPTCHA, key))) {
            log.info("找到验证码 验证正确");
            Habit.getCache().remove(CAPTCHA, key);
            return true;
        }
        log.error("没有找到验证码 验证错误");
        return false;
    }

    /**
     * TODO:上传失败删除文件
     *
     * @param files
     */
    public void delete(UploadFile[] files) {
        for (UploadFile file : files) {
            try {
                file.getFile().delete();
            } catch (Exception e) {
            }
        }
    }

    public void sendCode(){

    }
}
