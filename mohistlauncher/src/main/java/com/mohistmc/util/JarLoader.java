/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2024.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.util;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.jar.JarFile;

public class JarLoader {

    private static Instrumentation inst = null;

    public JarLoader() {
    }

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        JarLoader.inst = inst;
    }

    // Don't forget to specify -javaagent:<mohist jar> on Java 9+,
    // if you load the main Mohist jar from -cp rather than direct-jar
    public static void premain(String agentArgs, Instrumentation inst) {
        JarLoader.inst = inst;
    }

    // Mohist+ start - Use Arclight Forge Installer code
    private static java.lang.invoke.MethodHandle URLCLASSPATH_ADD_URL;
    public static void releaseAddURLHandle() { URLCLASSPATH_ADD_URL = null; }
    private static final boolean debug = Boolean.getBoolean("mohist.jarloader.debug");
    @SuppressWarnings("removal")
    // Mohist+ end
    public static void loadJar(Path path) {
        if (!path.toFile().getName().endsWith(".jar")) {
            return;
        }
        // Mohist+ start - Don't use Instrumentation for appending
        /*
        try {
            inst.appendToSystemClassLoaderSearch(new JarFile(path.toFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        if (debug) System.out.println("jarloader: Loading " + path.toAbsolutePath());
        try {
            ClassLoader loader = ClassLoader.getPlatformClassLoader();
            java.lang.reflect.Field ucpField;
            try {
                ucpField = loader.getClass().getDeclaredField("ucp");
            } catch (NoSuchFieldException e) {
                ucpField = loader.getClass().getSuperclass().getDeclaredField("ucp");
            }
            long offset = snw.srs.common.vm.Unsafe.getUnsafe().objectFieldOffset(ucpField);
            Object ucp = snw.srs.common.vm.Unsafe.getUnsafe().getObject(loader, offset);
            if (ucp == null) {
                var cl = Class.forName("jdk.internal.loader.URLClassPath");
                var handle = snw.srs.common.vm.Unsafe.getUnsafe().getImplLookup().findConstructor(cl, java.lang.invoke.MethodType.methodType(void.class, java.net.URL[].class, java.security.AccessControlContext.class));
                ucp = handle.invoke(new java.net.URL[]{}, (java.security.AccessControlContext) null);
                snw.srs.common.vm.Unsafe.getUnsafe().putObjectVolatile(loader, offset, ucp);
            }
            java.lang.reflect.Method method = ucp.getClass().getDeclaredMethod("addURL", java.net.URL.class);
            java.lang.invoke.MethodHandle addUrlMethod;
            if (URLCLASSPATH_ADD_URL == null) {
                System.out.println("jarloader: Saving addURL reference");
                URLCLASSPATH_ADD_URL = snw.srs.common.vm.Unsafe.getUnsafe().getImplLookup().unreflect(method);
            }
            addUrlMethod = URLCLASSPATH_ADD_URL;
            if (debug) System.out.println("jarloader: Invoking addURL with URL " + path.toUri().toURL());
            addUrlMethod.invoke(ucp, path.toUri().toURL());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // Mohist+ end
    }
}