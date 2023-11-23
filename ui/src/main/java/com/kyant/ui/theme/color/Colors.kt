package com.kyant.ui.theme.color

import androidx.compose.ui.graphics.Color

interface Colors {

    /**
     * Use primary roles for the most prominent components across the UI, such as the FAB, high-emphasis buttons, and active states.
     */
    val primaryColorRoles: Primary

    /**
     * Use secondary roles for less prominent components in the UI such as filter chips.
     */
    val secondaryColorRoles: Secondary

    /**
     * Use tertiary roles for contrasting accents that balance primary and secondary colors or bring heightened attention to an element such as an input field.
     */
    val tertiaryColorRoles: Tertiary

    /**
     * Use error roles to communicate error states, such as an incorrect password entered into a text field.
     */
    val errorColorRoles: Error

    /**
     * Use surface roles for more neutral backgrounds, and container colors for components like cards, sheets, and dialogs.
     */
    val surfaceColorRoles: Surface

    /**
     * Inverse roles are applied selectively to components to achieve colors that are the reverse of those in the surrounding UI, creating a contrasting effect.
     */
    val inverseColorRoles: Inverse

    /**
     * Inverse roles are used against surface.
     */
    val outlineColorRoles: Outline

    val primary: ColorSet
        get() = ColorSet(
            primaryColorRoles.color,
            primaryColorRoles.onColor
        )

    val primaryContainer: ColorSet
        get() = ColorSet(
            primaryColorRoles.container,
            primaryColorRoles.onContainer
        )

    val primaryFixed: ColorSet
        get() = ColorSet(
            primaryColorRoles.fixed,
            primaryColorRoles.onFixed,
            primaryColorRoles.onFixedVariant
        )

    val primaryFixedDim: ColorSet
        get() = ColorSet(
            primaryColorRoles.fixedDim,
            primaryColorRoles.onFixed,
            primaryColorRoles.onFixedVariant
        )

    val secondary: ColorSet
        get() = ColorSet(
            secondaryColorRoles.color,
            secondaryColorRoles.onColor
        )

    val secondaryContainer: ColorSet
        get() = ColorSet(
            secondaryColorRoles.container,
            secondaryColorRoles.onContainer
        )

    val secondaryFixed: ColorSet
        get() = ColorSet(
            secondaryColorRoles.fixed,
            secondaryColorRoles.onFixed,
            secondaryColorRoles.onFixedVariant
        )

    val secondaryFixedDim: ColorSet
        get() = ColorSet(
            secondaryColorRoles.fixedDim,
            secondaryColorRoles.onFixed,
            secondaryColorRoles.onFixedVariant
        )

    val tertiary: ColorSet
        get() = ColorSet(
            tertiaryColorRoles.color,
            tertiaryColorRoles.onColor
        )

    val tertiaryContainer: ColorSet
        get() = ColorSet(
            tertiaryColorRoles.container,
            tertiaryColorRoles.onContainer
        )

    val tertiaryFixed: ColorSet
        get() = ColorSet(
            tertiaryColorRoles.fixed,
            tertiaryColorRoles.onFixed,
            tertiaryColorRoles.onFixedVariant
        )

    val tertiaryFixedDim: ColorSet
        get() = ColorSet(
            tertiaryColorRoles.fixedDim,
            tertiaryColorRoles.onFixed,
            tertiaryColorRoles.onFixedVariant
        )

    val error: ColorSet
        get() = ColorSet(
            errorColorRoles.color,
            errorColorRoles.onColor
        )

    val errorContainer: ColorSet
        get() = ColorSet(
            errorColorRoles.container,
            errorColorRoles.onContainer
        )

    val surface: ColorSet
        get() = ColorSet(
            surfaceColorRoles.color,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceContainerLowest: ColorSet
        get() = ColorSet(
            surfaceColorRoles.containerLowest,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceContainerLow: ColorSet
        get() = ColorSet(
            surfaceColorRoles.containerLow,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceContainer: ColorSet
        get() = ColorSet(
            surfaceColorRoles.container,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceContainerHigh: ColorSet
        get() = ColorSet(
            surfaceColorRoles.containerHigh,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceContainerHighest: ColorSet
        get() = ColorSet(
            surfaceColorRoles.containerHighest,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceDim: ColorSet
        get() = ColorSet(
            surfaceColorRoles.dim,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val surfaceBright: ColorSet
        get() = ColorSet(
            surfaceColorRoles.bright,
            surfaceColorRoles.onColor,
            surfaceColorRoles.onColorVariant
        )

    val inverseSurface: ColorSet
        get() = ColorSet(
            inverseColorRoles.surface,
            inverseColorRoles.onSurface
        )

    val inversePrimary: ColorSet
        get() = ColorSet(
            inverseColorRoles.surface,
            inverseColorRoles.primary
        )

    val outline: ColorSet
        get() = ColorSet(
            outlineColorRoles.color
        )

    val outlineVariant: ColorSet
        get() = ColorSet(
            outlineColorRoles.variant
        )

    /**
     * Use primary roles for the most prominent components across the UI, such as the FAB, high-emphasis buttons, and active states.
     */
    interface Primary {

        /** High-emphasis fills, texts, and icons against surface */
        val color: Color

        /** Text and icons against primary */
        val onColor: Color

        /** Standout fill color against surface, for key components like FAB */
        val container: Color

        /** Text and icons against primary container */
        val onContainer: Color

        /** Fill colors against surface */
        val fixed: Color

        /** Higher-emphasis color against fill colors against surface */
        val fixedDim: Color

        /** Text and icons against primary fixed */
        val onFixed: Color

        /** Lower-emphasis color against primary fixed */
        val onFixedVariant: Color
    }

    /**
     * Use secondary roles for less prominent components in the UI such as filter chips.
     */
    interface Secondary {

        /** Less prominent fills, text, and icons against surface */
        val color: Color

        /** Text and icons against secondary */
        val onColor: Color

        /** Less prominent fill color against surface, for recessive components like tonal buttons */
        val container: Color

        /** Text and icons against secondary container */
        val onContainer: Color

        /** Fill colors against surface */
        val fixed: Color

        /** Higher-emphasis color for fill colors against surface */
        val fixedDim: Color

        /** Text and icons against secondary fixed */
        val onFixed: Color

        /** Lower-emphasis color against secondary fixed */
        val onFixedVariant: Color
    }

    /**
     * Use tertiary roles for contrasting accents that balance primary and secondary colors or bring heightened attention to an element such as an input field.
     */
    interface Tertiary {

        /** Complementary fills, text, and icons against surface */
        val color: Color

        /** Text and icons against tertiary */
        val onColor: Color

        /** Complementary container color against surface, for components like input fields */
        val container: Color

        /** Text and icons against tertiary container */
        val onContainer: Color

        /** Fill colors against surface */
        val fixed: Color

        /** Higher-emphasis color for fill colors against surface */
        val fixedDim: Color

        /** Text and icons against tertiary fixed */
        val onFixed: Color

        /** Lower-emphasis color against tertiary fixed */
        val onFixedVariant: Color
    }

    /**
     * Use error roles to communicate error states, such as an incorrect password entered into a text field.
     */
    interface Error {

        /** Attention-grabbing color against surface for fills, icons, and text, indicating urgency */
        val color: Color

        /** Text and icons against error */
        val onColor: Color

        /** Attention-grabbing fill color against surface */
        val container: Color

        /** Text and icons against error container */
        val onContainer: Color
    }

    /**
     * Use surface roles for more neutral backgrounds, and container colors for components like cards, sheets, and dialogs.
     */
    interface Surface {

        /** Default color for backgrounds */
        val color: Color

        /** Text and icons against any surface color */
        val onColor: Color

        /** Lower-emphasis color for text and icons against any surface color */
        val onColorVariant: Color

        /** Lowest-emphasis container color */
        val containerLowest: Color

        /** Low-emphasis container color */
        val containerLow: Color

        /** Default container color */
        val container: Color

        /** High-emphasis container color */
        val containerHigh: Color

        /** Highest-emphasis container color */
        val containerHighest: Color

        /** Dimmest surface color in light and dark themes */
        val dim: Color

        /** Brightest surface color in light and dark themes */
        val bright: Color
    }

    /**
     * Inverse roles are applied selectively to components to achieve colors that are the reverse of those in the surrounding UI, creating a contrasting effect.
     */
    interface Inverse {

        /** Background fills for elements which contrast against surface */
        val surface: Color

        /** Text and icons against inverse surface */
        val onSurface: Color

        /** Actionable elements, such as text buttons, against inverse surface */
        val primary: Color
    }

    /**
     * Inverse roles are used against surface.
     */
    interface Outline {

        /** Important boundaries, such as a text field outline */
        val color: Color

        /** Decorative elements, such as dividers */
        val variant: Color
    }
}
