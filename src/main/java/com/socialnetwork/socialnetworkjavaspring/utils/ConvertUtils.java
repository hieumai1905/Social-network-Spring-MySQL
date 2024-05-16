package com.socialnetwork.socialnetworkjavaspring.utils;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {

    public static <T> T convert(Object source, Class<T> dstClass) {
        if (source == null)
            return null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper.map(source, dstClass);
    }

    public static <T> List<T> convertList(List<?> sourceList, Class<T> dstClass) {
        if (sourceList == null) {
            return null;
        }

        List<T> outList = new ArrayList<>();
        for (Object object : sourceList) {
            outList.add(convert(object, dstClass));
        }

        return outList;
    }
}
