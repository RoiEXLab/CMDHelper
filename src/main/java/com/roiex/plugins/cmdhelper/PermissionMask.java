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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mask == null) ? 0 : mask.hashCode());
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PermissionMask) {
			PermissionMask mask = (PermissionMask) obj;
			return mask.mask.equals(mask) && mask.permission.equalsIgnoreCase(permission);
		}
		return false;
	}

	@Override
	public String toString() {
		return mask + "|" + permission;
	}
}
