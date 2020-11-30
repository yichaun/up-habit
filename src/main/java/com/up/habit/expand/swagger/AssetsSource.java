package com.up.habit.expand.swagger;

import com.jfinal.template.source.ClassPathSource;

import java.net.URL;

/**
 * TODO:
 * <p>
 * Created by 王剑洪 on 2020/2/1 20:49
 */
public class AssetsSource extends ClassPathSource {
    public AssetsSource(String fileName) {
        super(fileName);
    }

    public URL getUrl(){
        return this.url;
    }
}
