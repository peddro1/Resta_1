module Resta_1 {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.logging;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
