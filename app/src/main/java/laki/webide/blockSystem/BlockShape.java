package laki.webide.blockSystem;

/**
 * Defines the physical geometry types for blocks in the Laki Web IDE.
 * Replaces the hardcoded type IDs found in the original JAR.
 */
public enum BlockShape {
    /** Regular command block with top/bottom notches */
    COMMAND,
    /** "C" shaped block that holds a single stack of blocks (e.g., If, Repeat) */
    CONTAINER,
    /** "E" shaped block that holds two stacks (e.g., If-Else) */
    MULTI_CONTAINER,
    /** Rounded value block for Strings and Numbers */
    VALUE,
    /** Diamond shaped value block for Booleans */
    BOOLEAN,
    /** Rounded top block for Events/Initializers */
    HEAD,
    /** Block with no notches, used for stand-alone code bits */
    WRAPPER
}
