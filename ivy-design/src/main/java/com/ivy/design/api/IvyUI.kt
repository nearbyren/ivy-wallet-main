package com.ivy.design.api

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.ivy.design.IvyContext
import com.ivy.design.l0_system.IvyTheme

val LocalIvyContext = compositionLocalOf<IvyContext> { error("No LocalIvyContext") }

@Composable
fun IvyUI(
    design: IvyDesign,
    Content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    val ivyContext = design.context()

    //用于传递局部数据、配置和状态的重要工具
    CompositionLocalProvider(
        LocalIvyContext provides ivyContext,
    ) {
        IvyTheme(
            theme = ivyContext.theme,
            design = design
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                /***
                 * 其主要作用是帮助您控制和响应组合内部内容的约束条件。
                 * 它在布局中非常有用，
                 * 特别是在需要根据父容器的约束条件调整内部内容的大小或位置时。
                 */
                BoxWithConstraints {
                    ivyContext.screenWidth = with(LocalDensity.current) {
                        maxWidth.roundToPx()
                    }
                    ivyContext.screenHeight = with(LocalDensity.current) {
                        maxHeight.roundToPx()
                    }

                    Content()
                }
            }
        }
    }
}

@Composable
fun ivyContext(): IvyContext {
    return LocalIvyContext.current
}
