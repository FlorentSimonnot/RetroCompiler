/**
 * Writes java bytecode into a .class file.
 * @author CHU Jonathan
 *
 */
module writing {
    exports fr.project.writer;
    exports fr.project.warningObservers;
    requires org.objectweb.asm;
    requires instructions;
    requires optionsCommand;
}