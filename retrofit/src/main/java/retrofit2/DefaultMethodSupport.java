/*
 * Copyright (C) 2024 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import android.annotation.TargetApi;
import android.os.Build;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

class DefaultMethodSupport {
  boolean isDefaultMethod(Method method) {
    return false;
  }

  @Nullable
  Object invokeDefaultMethod(
      Method method, Class<?> declaringClass, Object proxy, @Nullable Object[] args)
      throws Throwable {
    throw new AssertionError();
  }

  /**
   * Android does not support MR jars, so this extends the baseline JVM support which targets
   * Java 8 APIs. Default methods and the reflection API to detect them were added to API 24
   * as part of the initial Java 8 set. MethodHandle, our means of invoking the default method
   * through the proxy, was not added until API 26.
   */
  @TargetApi(24)
  static final class Android24 extends DefaultMethodSupportJvm {
    @Override
    Object invokeDefaultMethod(
        Method method, Class<?> declaringClass, Object proxy, @Nullable Object[] args)
        throws Throwable {
      if (Build.VERSION.SDK_INT < 26) {
        throw new UnsupportedOperationException(
            "Calling default methods on API 24 and 25 is not supported");
      }
      return super.invokeDefaultMethod(method, declaringClass, proxy, args);
    }
  }
}
