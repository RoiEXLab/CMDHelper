package com.roiex.plugins.cmdhelper;

import java.util.ArrayList;
import java.util.Collection;

public class StructuredCommand {

	private Collection<CommandBranch> branches = new ArrayList<>();

	public StructuredCommand(CommandBranch... branches) {
		for (CommandBranch branch : branches) {
			this.branches.add(branch);
		}
	}

	public StructuredCommand addBranch(CommandBranch branch) {
		branches.add(branch);
		return this;
	}

	protected Collection<CommandBranch> getBranches() {
		return branches;
	}
}
