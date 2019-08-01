/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/Apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */


package com.tencent.angel.spark.context

import com.tencent.angel.ml.math2.utils.RowType
import com.tencent.angel.spark.models.PSVector
import com.tencent.angel.spark.{PSFunSuite, SharedPSContext}

class PSVectorPoolSuite extends PSFunSuite with SharedPSContext {

  test("allocate") {
    val dim = 10
    val capacity = 10
    val pool = new PSVectorPool(0, dim, capacity, RowType.T_DOUBLE_DENSE)

    var proxys: Array[PSVector] = null

    try {
      proxys = (0 until capacity).toArray.map { _ =>
        pool.allocate()
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
        assert(true)
    }

    val releaseNum = 5
    proxys.slice(0, releaseNum).foreach { key =>
      pool.delete(key)
    }

    try {
      (0 until capacity - releaseNum).foreach { i =>
        pool.allocate()
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
        assert(true)
    }

    proxys.slice(releaseNum, capacity).foreach { key =>
      pool.delete(key)
    }
  }
}