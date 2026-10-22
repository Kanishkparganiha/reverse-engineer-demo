cd ~/data_science/AI/rea_demo
T=tools/scala-2.13.16

# 1. Compile the Scala source into .class files
java -cp "$T/scala-compiler.jar:$T/scala-library.jar:$T/scala-reflect.jar" scala.tools.nsc.Main -usejavacp -d build src/CleanseJob.scala

# 2. Package the .class files into the JAR
jar cfe customer-cleanse.jar CleanseJob -C build .

# 3. Run the forward transform
java -cp "customer-cleanse.jar:$T/scala-library.jar" CleanseJob input.csv output.csv
