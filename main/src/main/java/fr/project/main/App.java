package fr.project.main;

import fr.project.detection.classVisitor.MyClassVisitor;
import fr.project.detection.observers.*;
import fr.project.detection.visitor.MyVisitor;
import fr.project.optionsCommand.Option;
import fr.project.optionsCommand.OptionFactory;
import fr.project.optionsCommand.Options;
import fr.project.optionsCommand.OptionsParser;
import fr.project.parsing.parser.FileParser;
import fr.project.parsing.parser.ParserException;
import fr.project.warningObservers.*;
import fr.project.writer.MyRecordClassWriter;
import fr.project.writer.MyWriter;
import fr.project.writer.Writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SIMONNOT Florent
 * A class that represents the project Retro.
 * It can take a directory, a .class file or a .jar file as input and detect all the features detected on it according to the option [--features].
 * [--features] contains a list of features that can be try-with-resources, lambda, nest mates, concatenation or record.
 * If [--features] is absent, then all the features mentioned above are asked.
 * It has to have a [--target] version of java which you want to write your new bytecode files.
 * The option [--info] displays all the features detected for each file.
 * The option [--force] forces the writing of new bytecode files even if the java version is not compatible.
 *
 */
public class App {

	public static void main(String[] args) throws IOException, ParserException {
		List<MyClassVisitor> visitors = new ArrayList<>();

		var optionFactory = createOptionFactory();
		var observersFactory = createObserverFactory();

		var options = OptionsParser.parseOptions(args, optionFactory);

		if(options.helpIsDemanding()){
			Option.showHelp();
			return;
		}

		var observers = FeaturesManager.createObservers(options.getArgsOption(Option.OptionEnum.FEATURES), observersFactory);

		var files = FileParser.parseFile(options.getFile());

		/*      ************************************************     */
		/*         SHOW JAVA VERSION USE FOR WRITE THE FILE(S)       */
		/*      ************************************************     */

		if(options.noOptionsAreDemanding()){
			files.forEach(f -> {
				try {
					System.out.println(f.getName() + " was written in Java " + f.getVersion());
				} catch (IOException e) {
					System.err.println("We can't read the file " + f.getName());
				}
			});
			return;
		}

		/*      ************************************************     */
		/*           GET INSTRUCTIONS AND OBSERVES FEATURES          */
		/*      ************************************************     */

		files.forEach(f -> {
			var mv = new MyVisitor(f, observers);
			var cv = mv.getClassVisitor();
			try {
				mv.getClassReader().accept(cv, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			visitors.add(cv);
		});

		if(options.infoIsDemanding())
			observers.forEach(FeatureObserver::showFeatures);


		/*      ************************************************     */
		/*        CREATE A NEW CLASS FILE WITH A NEW VERSION         */
		/*      ************************************************     */

		if(options.rewritingIsDemanding()){
			var version = convertTargetToOpcode(options.getArgsOption(Option.OptionEnum.TARGET));
			var warnings = WarningsManager.createObservers(options, createWarningFactory());

			visitors.forEach(cv -> {

				Writer myWriter = new MyWriter(cv.getMyClass(), version, warnings, options);

				if(cv.getMyClass().isRecordClass()){
					myWriter = new MyRecordClassWriter(myWriter);
				}

				//var myWriter = new MyWriter(cv.getMyClass(), version, warnings, options);

				myWriter.createClass();
				myWriter.writeFields();
				myWriter.writeLambdaInnerClasses();
				myWriter.writeLambdaFiles();
				myWriter.writeConstructors();
				myWriter.writeMethods();

				if(warningsFound(warnings)){
					System.out.println("We can't compile " + cv.getMyClass().getClassName() + ".java in java " + (version-44) + " because we found " + countWarningsFound(warnings) + " warning(s)");
					warnings.forEach(WarningObserver::showWarning);
				}
				else{
					String res = null;
					try {
						res = myWriter.createFile();
						showEndMessage(options, res);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	private static OptionFactory createOptionFactory(){
		var optionFactory = new OptionFactory();
		optionFactory.register("--help", new Option(Option.OptionEnum.HELP));
		optionFactory.register("--info", new Option(Option.OptionEnum.INFO));
		optionFactory.register("--target", new Option(Option.OptionEnum.TARGET));
		optionFactory.register("--features", new Option(Option.OptionEnum.FEATURES));
		optionFactory.register("--force", new Option(Option.OptionEnum.FORCE));
		return optionFactory;
	}

	private static FeaturesObserverFactory createObserverFactory(){
		var observersFactory = new FeaturesObserverFactory();
		observersFactory.register("try-with-resources", new TryWithResourcesObserver());
		observersFactory.register("nestMember", new NestMemberObserver());
		observersFactory.register("concatenation", new ConcatenationObserver());
		observersFactory.register("lambda", new LambdaObserver());
		observersFactory.register("record", new RecordObserver());
		return observersFactory;
	}

	private static WarningObserverFactory createWarningFactory(){
		var observersFactory = new WarningObserverFactory();
		observersFactory.register("try-with-resources", new WarningTryWithResourcesObserver());
		observersFactory.register("nestMember", new WarningNestMemberObserver());
		observersFactory.register("concatenation", new WarningConcatenationObserver());
		observersFactory.register("lambda", new WarningLambdaObserver());
		observersFactory.register("record", new WarningRecordObserver());
		return observersFactory;
	}

	private static int convertTargetToOpcode(String s){
		return Integer.parseInt(s)+44;
	}

	private static boolean warningsFound(List<WarningObserver> warnings){
		return warnings.stream().mapToInt(WarningObserver::numberOfWarningsDetected).sum() > 0;
	}

	private static int countWarningsFound(List<WarningObserver> warnings){
		return warnings.stream().mapToInt(WarningObserver::numberOfWarningsDetected).sum();
	}

	private static void showEndMessage(Options options, String file){
		if(options.forceIsDemanding()){
			System.out.println("Force option was used to retro compile your file(s).");
		}
		System.out.println("You can find your file in " + file);
	}
}
