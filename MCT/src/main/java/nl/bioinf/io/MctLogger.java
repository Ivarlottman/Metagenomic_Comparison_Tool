package nl.bioinf.io;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * @author Ivar Lottman
 * @version 1
 * Mct Logger class
 */
public class MctLogger {
    private static final Logger logger = LogManager.getLogger(MctLogger.class);
    public void applyLogger(int verbosity) {
        if (verbosity > 2 || verbosity < 0) {throw new IllegalArgumentException("Verbosity option lengts must be between 0 and 2");}
        else if (verbosity == 2) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
            logger.debug("Verbosity level set to DEBUG");
        } else if (verbosity == 1) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.INFO);
            logger.info("Verbosity level set to INFO");
        } else if (verbosity == 0) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.WARN);
            logger.warn("Verbosity level set to WARN");
        }
    }
}
