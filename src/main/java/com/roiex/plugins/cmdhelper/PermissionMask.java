package com.roiex.plugins.cmdhelper;

import com.google.common.base.Preconditions;

public class PermissionMask {

	private String mask;
	private String permission;

	public PermissionMask(String mask, String permission) {
		StructureParser.validateStaticSyntax(Preconditions.checkNotNull(mask));
		this.mask = mask;
		this.permission = Preconditions.checkNotNull(permission);
	}

	public String getMask() {
		return mask;
	}

	public String getPermission() {
		return permission;
	}
}
