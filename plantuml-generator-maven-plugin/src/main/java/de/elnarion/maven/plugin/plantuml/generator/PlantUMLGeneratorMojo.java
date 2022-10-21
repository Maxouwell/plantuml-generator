package de.elnarion.maven.plugin.plantuml.generator;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import de.elnarion.util.plantuml.generator.PlantUMLClassDiagramGenerator;
import de.elnarion.util.plantuml.generator.classdiagram.ClassifierType;
import de.elnarion.util.plantuml.generator.classdiagram.VisibilityType;
import de.elnarion.util.plantuml.generator.config.PlantUMLClassDiagramConfigBuilder;

/**
 * This Mojo is used as maven frontend of the PlantUMLClassDiagramGenerator in
 * the artifact plantuml-generator-util.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE, requiresProject = true)
public class PlantUMLGeneratorMojo extends AbstractPlantUMLGeneratorMojo {

	/** The hide fields. */
	@Parameter(property = PREFIX + "hideFields", defaultValue = "false", required = false)
	private boolean hideFields;

	/** The hide methods. */
	@Parameter(property = PREFIX + "hideMethods", defaultValue = "false", required = false)
	private boolean hideMethods;

	/** The scan packages. */
	@Parameter(property = PREFIX + "scanPackages", defaultValue = "", required = true)
	private List<String> scanPackages;

	/** The whitelist regular expression for the scan packages. */
	@Parameter(property = PREFIX + "whitelistRegexp", defaultValue = "", required = false)
	private String whitelistRegexp;

	/** The blacklist regular expression for the scan packages. */
	@Parameter(property = PREFIX + "blacklistRegexp", defaultValue = "", required = false)
	private String blacklistRegexp;

	/** The hide classes. */
	@Parameter(property = PREFIX + "hideClasses", defaultValue = "", required = false)
	private List<String> hideClasses;

	/** the list of all field classifiers to ignore. */
	@Parameter(property = PREFIX + "fieldClassifierListToIgnore", defaultValue = "", required = false)
	private List<ClassifierType> fieldClassifierListToIgnore;

	/** the list of all method classifiers to ignore. */
	@Parameter(property = PREFIX + "methodClassifierListToIgnore", defaultValue = "", required = false)
	private List<ClassifierType> methodClassifierListToIgnore;

	/** The remove methods. */
	@Parameter(property = PREFIX + "removeMethods", defaultValue = "false", required = false)
	private boolean removeMethods;

	/** The remove methods. */
	@Parameter(property = PREFIX + "addJPAAnnotations", defaultValue = "false", required = false)
	private boolean addJPAAnnotations;

	/** The remove methods. */
	@Parameter(property = PREFIX + "removeFields", defaultValue = "false", required = false)
	private boolean removeFields;

	/** The remove methods. */
	@Parameter(property = PREFIX + "fieldBlacklistRegexp", defaultValue = "", required = false)
	private String fieldBlacklistRegexp = null;

	/** The remove methods. */
	@Parameter(property = PREFIX + "methodBlacklistRegexp", defaultValue = "", required = false)
	private String methodBlacklistRegexp = null;

	/** The remove methods. */
	@Parameter(property = PREFIX + "maxVisibilityFields", defaultValue = "PRIVATE", required = false)
	private VisibilityType maxVisibilityFields = VisibilityType.PRIVATE;

	/** The remove methods. */
	@Parameter(property = PREFIX + "maxVisibilityMethods", defaultValue = "PRIVATE", required = false)
	private VisibilityType maxVisibilityMethods = VisibilityType.PRIVATE;


	/**
	 * Execute.
	 *
	 * @throws MojoExecutionException the mojo execution exception
	 * @throws MojoFailureException   the mojo failure exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting plantuml generation ");
		try {
			ClassLoader loader = getCompileClassLoader();
			PlantUMLClassDiagramGenerator classDiagramGenerator;
			PlantUMLClassDiagramConfigBuilder configBuilder;
			if (whitelistRegexp == null || "".equals(whitelistRegexp)) {
				configBuilder = new PlantUMLClassDiagramConfigBuilder(
						((blacklistRegexp != null && !"".equals(blacklistRegexp)) ? blacklistRegexp : null),
						scanPackages);
			} else {
				configBuilder = new PlantUMLClassDiagramConfigBuilder(scanPackages, whitelistRegexp);
			}
			configBuilder.withClassLoader(loader).withHideClasses(hideClasses).withHideFieldsParameter(hideFields)
					.withHideMethods(hideMethods).addFieldClassifiersToIgnore(fieldClassifierListToIgnore)
					.addMethodClassifiersToIgnore(methodClassifierListToIgnore).withRemoveFields(removeFields)
					.withRemoveMethods(removeMethods).withFieldBlacklistRegexp(fieldBlacklistRegexp)
					.withMethodBlacklistRegexp(methodBlacklistRegexp).withMaximumFieldVisibility(maxVisibilityFields)
					.withMaximumMethodVisibility(maxVisibilityMethods).withJPAAnnotations(addJPAAnnotations)
					.addAdditionalPlantUmlConfigs(getAdditionalPlantUmlConfigs());
			classDiagramGenerator = new PlantUMLClassDiagramGenerator(configBuilder.build());
			String classDiagramText = classDiagramGenerator.generateDiagramText();
			writeDiagramToFile(classDiagramText);
		} catch (Exception e) {
			getLog().error("Exception:" + e.getMessage());
			getLog().error(e);
		}
	}

	/**
	 * Checks if is hide fields.
	 *
	 * @return true, if is hide fields
	 */
	public boolean isHideFields() {
		return hideFields;
	}

	/**
	 * Sets the hide fields.
	 *
	 * @param hideFields the hide fields
	 */
	public void setHideFields(boolean hideFields) {
		this.hideFields = hideFields;
	}

	/**
	 * Checks if is hide methods.
	 *
	 * @return true, if is hide methods
	 */
	public boolean isHideMethods() {
		return hideMethods;
	}

	/**
	 * Sets the hide methods.
	 *
	 * @param hideMethods the hide methods
	 */
	public void setHideMethods(boolean hideMethods) {
		this.hideMethods = hideMethods;
	}

	/**
	 * Gets the scan packages.
	 *
	 * @return List - the scan packages
	 */
	public List<String> getScanPackages() {
		return scanPackages;
	}

	/**
	 * Sets the scan packages.
	 *
	 * @param scanPackages the scan packages
	 */
	public void setScanPackages(List<String> scanPackages) {
		this.scanPackages = scanPackages;
	}

	/**
	 * Gets the hide classes.
	 *
	 * @return List - the hide classes
	 */
	public List<String> getHideClasses() {
		return hideClasses;
	}

	/**
	 * Sets the hide classes.
	 *
	 * @param hideClasses the hide classes
	 */
	public void setHideClasses(List<String> hideClasses) {
		this.hideClasses = hideClasses;
	}


	/**
	 * Gets the whitelist regexp.
	 *
	 * @return the whitelist regexp
	 */
	public String getWhitelistRegexp() {
		return whitelistRegexp;
	}

	/**
	 * Sets the whitelist regexp.
	 *
	 * @param whitelistRegexp the new whitelist regexp
	 */
	public void setWhitelistRegexp(String whitelistRegexp) {
		this.whitelistRegexp = whitelistRegexp;
	}

	/**
	 * Gets the blacklist regexp.
	 *
	 * @return the blacklist regexp
	 */
	public String getBlacklistRegexp() {
		return blacklistRegexp;
	}

	/**
	 * Sets the blacklist regexp.
	 *
	 * @param blacklistRegexp the new blacklist regexp
	 */
	public void setBlacklistRegexp(String blacklistRegexp) {
		this.blacklistRegexp = blacklistRegexp;
	}


	/**
	 * Checks if is adds the JPA annotations.
	 *
	 * @return true, if is adds the JPA annotations
	 */
	public boolean isAddJPAAnnotations() {
		return addJPAAnnotations;
	}

	/**
	 * Sets the adds the JPA annotations.
	 *
	 * @param addJPAAnnotations the new adds the JPA annotations
	 */
	public void setAddJPAAnnotations(boolean addJPAAnnotations) {
		this.addJPAAnnotations = addJPAAnnotations;
	}

}
