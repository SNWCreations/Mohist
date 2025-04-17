/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.server.permission.handler;

import java.util.Collection;
import net.minecraftforge.server.permission.nodes.PermissionNode;

@FunctionalInterface
public interface IPermissionHandlerFactory
{
    IPermissionHandler create(Collection<PermissionNode<?>> permissions);
}
