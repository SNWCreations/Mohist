package com.mohistmc.paper.util;

import java.io.InputStream;

// Mohist+ - This is not same as the Paper's class with the same name because we don't need their methods for this class
public class MappingEnvironment {
    public static InputStream srg2mojangMappingsStream() {
        return MappingEnvironment.class.getClassLoader().getResourceAsStream("mappings/srg2mojang.srg");
    }
}
