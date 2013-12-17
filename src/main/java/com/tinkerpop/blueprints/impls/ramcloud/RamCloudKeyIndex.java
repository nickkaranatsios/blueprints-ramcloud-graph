/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tinkerpop.blueprints.impls.ramcloud;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.impls.ramcloud.RamCloudGraph;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author yoshitomo
 */
public class RamCloudKeyIndex<T extends RamCloudElement> extends RamCloudIndex<T> implements Serializable {
    private RamCloudGraph graph;
	
    public RamCloudKeyIndex(long tableId, String indexName, Object propValue, RamCloudGraph graph, Class<T> indexClass) {
	super(tableId, indexName, propValue, graph, indexClass);
	this.graph = graph;
    }
    
    public RamCloudKeyIndex(long tableId, byte[] rcKey, RamCloudGraph graph, Class<T> indexClass) {
	super(tableId, rcKey, graph, indexClass);
	this.graph = graph;
    }

    public void autoUpdate(final String key, final Object newValue, final Object oldValue, final T element) {
	if (graph.indexedKeys.contains(key)) {
	    if (oldValue != null) {
		this.remove(key, oldValue, element);
	    }
	    this.put(key, newValue, element);
	}
    }

    public void autoRemove(final String key, final Object oldValue, final T element) {
	if (graph.indexedKeys.contains(key)) {
	    this.remove(key, oldValue, element);
	}
    }

    public long reIndexElements(final RamCloudGraph graph, final Iterable<? extends Element> elements, final Set<String> keys) {
	long counter = 0;
	for (final Element element : elements) {
	    for (final String key : keys) {
		final Object value = element.removeProperty(key);
		if (null != value) {
		    counter++;
		    element.setProperty(key, value);
		}
	    }
	}
	return counter;
    }
    
}
