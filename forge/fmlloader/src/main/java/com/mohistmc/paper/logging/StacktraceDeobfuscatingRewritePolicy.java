package com.mohistmc.paper.logging;

import java.lang.invoke.MethodHandle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;

@Plugin(
        name = "StacktraceDeobfuscatingRewritePolicy",
        category = Core.CATEGORY_NAME,
        elementType = "rewritePolicy",
        printObject = true
)
public final class StacktraceDeobfuscatingRewritePolicy implements RewritePolicy {
    public static final boolean disabled = Boolean.getBoolean("mohist.stackdeobf.disabled");
    private static final Logger log = LogManager.getLogger(StacktraceDeobfuscatingRewritePolicy.class);
    public static MethodHandle DEOBFUSCATE_THROWABLE;

    private StacktraceDeobfuscatingRewritePolicy() {
    }

    @Override
    public LogEvent rewrite(final LogEvent rewrite) {
        if (!disabled) {
            final Throwable thrown = rewrite.getThrown();
            if (thrown != null) {
                deobfuscateThrowable(thrown);
                return new Log4jLogEvent.Builder(rewrite)
                        .setThrownProxy(null)
                        .build();
            }
        }
        return rewrite;
    }

    private static void deobfuscateThrowable(final Throwable thrown) {
        if (disabled) {
            return;
        }
        if (DEOBFUSCATE_THROWABLE == null) {
            // Maybe loggers were used before Minecraft server was booted
            return;
        }
        try {
            DEOBFUSCATE_THROWABLE.invoke(thrown);
        } catch (final Error e) {
            throw e;
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @PluginFactory
    public static StacktraceDeobfuscatingRewritePolicy createPolicy() {
        if (disabled) {
            log.warn("Stacktrace deobfuscating is explicitly disabled with -Dmohist.stackdeobf.disabled=true");
        }
        return new StacktraceDeobfuscatingRewritePolicy();
    }
}