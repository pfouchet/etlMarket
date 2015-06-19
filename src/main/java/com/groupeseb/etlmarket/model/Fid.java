package com.groupeseb.etlmarket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Fid {
	private String functionalId;
	private String sourceSystem;
	private String version;
}
