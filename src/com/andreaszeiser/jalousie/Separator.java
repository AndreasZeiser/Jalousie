/*
 * Copyright (C) 2012 Andreas Zeiser
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

package com.andreaszeiser.jalousie;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class separates the visible and the non-visible content within
 * LinearLayoutJalousie. 
 * 
 * @author Andreas Zeiser
 * 
 */
public final class Separator extends View {

	public Separator(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
	}

	public Separator(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public Separator(Context context) {

		super(context);
	}

}
