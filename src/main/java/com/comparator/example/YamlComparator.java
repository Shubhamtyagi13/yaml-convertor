package com.comparator.example;

import org.yaml.snakeyaml.Yaml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class YamlComparator {
    public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException, IntrospectionException {
        Yaml yaml = new Yaml();
        
        InputStream in = YamlComparator.class
                .getResourceAsStream("/sample.yml");
        Sample sample1 = yaml.loadAs(in, Sample.class);
        
        InputStream in2 = YamlComparator.class
                .getResourceAsStream("/sample2.yml");
        Sample sample2 = yaml.loadAs(in2, Sample.class);
        compareYaml(sample1, sample2, "property1", "property2", "property3");
    }
    
    private static void compareYaml(Object yaml1, Object yaml2, String... propertyNames)
            throws IntrospectionException,
            IllegalAccessException, InvocationTargetException {
          Set<String> names = new HashSet<String>(Arrays
              .asList(propertyNames));
          BeanInfo beanInfo = Introspector.getBeanInfo(yaml1
              .getClass());
          for (PropertyDescriptor prop : beanInfo
              .getPropertyDescriptors()) {
            if (names.remove(prop.getName())) {
              Method getter = prop.getReadMethod();
              Object value1 = getter.invoke(yaml1);
              Object value2 = getter.invoke(yaml2);
              if (value1 == value2
                  || (value1 != null && value1.equals(value2))) {
                continue;
              }

              System.out.println("Difference in "+ prop.getName() +" Value of yaml1 = "+value1 +" : Value of yaml2 = "+value2);
            }
          }
        }
}