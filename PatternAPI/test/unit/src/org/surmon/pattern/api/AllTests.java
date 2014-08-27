/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.surmon.pattern.api;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.opencv.core.Core;

/**
 *
 * @author palasjiri
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    org.surmon.pattern.api.ImageConverterTest.class,
    org.surmon.pattern.api.utils.MatUtilsTest.class})

public class AllTests {

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
}
