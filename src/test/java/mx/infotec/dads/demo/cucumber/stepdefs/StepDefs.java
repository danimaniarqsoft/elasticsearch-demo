package mx.infotec.dads.demo.cucumber.stepdefs;

import mx.infotec.dads.demo.DemoApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = DemoApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
