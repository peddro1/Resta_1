module Resta_1 {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.logging;
	requires javafx.graphics;
	requires java.rmi;
	
	exports application;
	opens application to javafx.graphics, javafx.fxml;
}
