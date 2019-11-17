package com.meetkiki.blog.bootstrap;

import jetbrick.io.resource.ClasspathResource;
import jetbrick.io.resource.Resource;
import jetbrick.template.loader.AbstractResourceLoader;
import jetbrick.util.PathUtils;

public class MyResourceLoader extends AbstractResourceLoader {
        public MyResourceLoader() {
            root = "templates";
            reloadable = false;
        }

        @Override
        public Resource load(String name) {
            String path = name;
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (!name.startsWith(root)){
                path = PathUtils.concat(root, name);
            }

            ClasspathResource resource = new ClasspathResource(path);
            if (!resource.exist()) {
                return null;
            }
            resource.setRelativePathName(name); // use relative name
            return resource;
        }
    }