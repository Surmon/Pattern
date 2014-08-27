/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author palasjiri
 * @param <E>
 */
public class IMap<E> extends HashMap<Integer, E>{
    
    private int last = 0;
    private Deque<Integer> missing = new ArrayDeque<>();
           
    public void put(E object) {
        Integer id;
        if(!missing.isEmpty()){
            id = missing.poll();
        }else{
            id = last++;
        }
        
        put(id, object);
    }
    
    public E remove(Integer id) {
        E removed = super.remove(id);
        if(removed != null){
            if(id == last){
                last++;
            }else{
                missing.add(id);
            }
            return removed;
        }else{
            return null;
        }
    }
    
    public void addList(List<E> list) {
        for (E e : list) {
            put(e);
        }
    }
    
    
    
    
}
