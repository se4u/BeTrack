# Material Stepper

[![](https://jitpack.io/v/fcannizzaro/material-stepper.svg)](https://jitpack.io/#fcannizzaro/material-stepper)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Material%20Stepper-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3162)
[![git-card](http://goo.gl/2TUzYL)](http://fcannizzaro.github.io/git-card/get?user=fcannizzaro&repo=material-stepper)

<!--
  GIT-CARD
  title: Material Stepper
  cover: https://dl.dropboxusercontent.com/u/73317452/dev/material_stepper.png
  counter_issues: false
  color: #3f51b5
  -->
  
## Dependency

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Add the dependency

```gradle
dependencies {
   compile 'com.github.fcannizzaro:material-stepper:1.1.8'
}
```

## Preview

![text](https://github.com/FrancisCan/MaterialStepper/blob/master/screenshot/text.png?raw=true)
![progress](https://github.com/FrancisCan/MaterialStepper/blob/master/screenshot/progress.png?raw=true)
![dots](https://github.com/FrancisCan/MaterialStepper/blob/master/screenshot/dots.png?raw=true)
![tabs](https://github.com/FrancisCan/MaterialStepper/blob/master/screenshot/tabs.png?raw=true)

# Stepper
Extend one of these classes ```TextStepper```, ```ProgressStepper```, ```DotStepper```, ```TabStepper```

## Methods

#### setTitle(String)
set stepper title

#### addStep(String)
add a step

#### setPrimaryColor(int)
set primary color (Toolbar/ Progress & Dot & Tab Circle color )

#### setDarkPrimaryColor(int)
set status bar & text color

#### setErrorTimeout(int)
set timeout (milliseconds) of error message

#### setStateAdapter()
use FragmentStatePagerAdapter for ViewPager instead of FragmentPagerAdapter

#### setStartPreviousButton()
show initial "previous" button (first step)

## Tab Stepper Methods

#### setLinear(boolean)
limit user path (user has to complete previous step before move to next)

#### setPreviousVisible()
show previous button on the left

#### setDisabledTouch()
disable user touch on tabs (prevent click)

#### setAlternativeTab(boolean)
use alternative tab style

 - classic

 ![classic](https://github.com/FrancisCan/MaterialStepper/blob/master/screenshot/classic.png?raw=true)

 - alternative

 ![alternative](https://github.com/FrancisCan/MaterialStepper/blob/master/screenshot/alternative.png?raw=true)

## Override Method

#### void onComplete()
#### void onComplete(Bundle data)
called when 'complete' button is pressed

```java
public class Sample extends TabStepper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	/*
		...
		
		methods
		
		...
	*/
	
	// call super at the end
	super.onCreate(savedInstanceState);
    }

```

# Step
Extend ```AbstractStep```

## Override Methods

#### String name()
override step name

#### String error()
override error message (HTML supported)

#### boolean isOptional()
override if is optional [Default = false]

#### boolean nextIf()
override condition to move to next step [Default = true]

#### void onStepVisible()
called when a step is visible

#### void onNext()
called when next button is pressed

#### void onPrevious()
called when previous button is pressed

```java
public class StepSample extends AbstractStep {

    // step name
    @Override
    public String name() {
        return "Step title";
    }

    // step optional title (default: "Optional")
    @Override
    public String optional() {
        return "Optional subtitle";
    }
    
    // set step optional or not (default: false)
    @Override
    public boolean isOptional() {
        return true;
    }

    // override only if step is limited by condition
    @Override
    public boolean nextIf() {
        return i > 5;
    }

    // error showed on change next step ( see nextIf )
    @Override
    public String error() {
        return "<b>Condition</b>";
    }

    // do something when step is visible
    @Override
    public void onStepVisible() {
        super.onStepVisible();
    }

}
```

# Share Data between steps

#### Bundle getStepData()
get available bundle for current step

#### Bundle getStepDataFor(int step)
get bundle for specific step and set your data

#### Bundle getLastStepData()
get bundle for last step

```java
// get bundle for step 3
Bundle data = getStepDataFor(3);
data.putInt("test",5);
```

# Save Stepper Result
 Save data returned in ```onActivityResult```
```java
Bundle data = mStepper.getExtras();
```

# Strings
Overwrite these strings for support multilanguage / custom titles
```xml
<string name="ms_prev">PREV</string>
<string name="ms_next">NEXT</string>
<string name="ms_end">COMPLETE</string>
<string name="ms_continue">CONTINUE</string>
<string name="ms_optional">Optional</string>
<string name="ms_text_step">Step $current of $total</string>
```
