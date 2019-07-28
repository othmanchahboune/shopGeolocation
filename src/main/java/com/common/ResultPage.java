package com.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;

@Data
public class ResultPage<T> {
	private List<T> items = new ArrayList<>();
	private Long total = 0L;

	public ResultPage(final Collection<? extends T> content) {
		this.setDatas(content, (long) content.size());
	}

	public ResultPage(final Collection<? extends T> content, final Long total) {
		this.setDatas(content, total);
	}

	public ResultPage() {
	}

	private ResultPage<T> setDatas(final Collection<? extends T> content, final Long total) {
		if (null == content) {
			throw new IllegalArgumentException("Content cannot be null");
		}

		this.items.addAll(content);

		this.total = total;

		return this;
	}
}
