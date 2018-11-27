/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.android.settings.homepage.contextualcards.conditional;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.settings.homepage.contextualcards.ContextualCard;
import com.android.settings.homepage.contextualcards.ControllerRendererPool;
import com.android.settings.testutils.SettingsRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

@RunWith(SettingsRobolectricTestRunner.class)
public class ConditionHeaderContextualCardRendererTest {

    @Mock
    private ControllerRendererPool mControllerRendererPool;
    @Mock
    private ConditionContextualCardController mController;
    private Context mContext;
    private ConditionHeaderContextualCardRenderer mRenderer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = spy(RuntimeEnvironment.application);
        mRenderer = new ConditionHeaderContextualCardRenderer(mContext, mControllerRendererPool);
    }

    @Test
    public void bindView_shouldSetClickListener() {
        final int viewType = mRenderer.getViewType(false /* isHalfWidth */);
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final View view = LayoutInflater.from(mContext).inflate(viewType, recyclerView, false);
        final RecyclerView.ViewHolder viewHolder = mRenderer.createViewHolder(view);
        when(mControllerRendererPool.getController(mContext,
                ContextualCard.CardType.CONDITIONAL_HEADER)).thenReturn(mController);

        mRenderer.bindView(viewHolder, generateConditionHeaderContextualCard());

        assertThat(viewHolder.itemView).isNotNull();
        assertThat(viewHolder.itemView.hasOnClickListeners()).isTrue();
    }

    @Test
    public void bindView_clickView_shouldSetTrueToIsConditionExpanded() {
        final int viewType = mRenderer.getViewType(false /* isHalfWidth */);
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final View view = LayoutInflater.from(mContext).inflate(viewType, recyclerView, false);
        final RecyclerView.ViewHolder viewHolder = mRenderer.createViewHolder(view);
        when(mControllerRendererPool.getController(mContext,
                ContextualCard.CardType.CONDITIONAL_HEADER)).thenReturn(mController);

        mRenderer.bindView(viewHolder, generateConditionHeaderContextualCard());

        assertThat(viewHolder.itemView).isNotNull();
        viewHolder.itemView.performClick();

        verify(mController).setIsExpanded(true);
        verify(mController).onConditionsChanged();
    }

    private ContextualCard generateConditionHeaderContextualCard() {
        return new ConditionHeaderContextualCard.Builder()
                .setConditionalCards(generateConditionCards(3))
                .setName("test_condition_header")
                .setRankingScore(-9999.0)
                .build();
    }

    private List<ContextualCard> generateConditionCards(int numberOfCondition) {
        final List<ContextualCard> conditionCards = new ArrayList<>();
        for (int i = 0; i < numberOfCondition; i++) {
            conditionCards.add(new ConditionalContextualCard.Builder()
                    .setConditionId(123 + i)
                    .setMetricsConstant(1)
                    .setActionText("test_action" + i)
                    .setName("test_name" + i)
                    .setTitleText("test_title" + i)
                    .setSummaryText("test_summary" + i)
                    .setIsHalfWidth(true)
                    .build());
        }
        return conditionCards;
    }
}