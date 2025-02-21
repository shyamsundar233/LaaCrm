#!/bin/sh

# Check if two arguments are provided
if [ $# -ne 2 ]; then
  echo "Usage: sh create-component.sh <directory> <component-name>"
  exit 1
fi

# Assign arguments to variables
TARGET_DIR=$1
COMPONENT_NAME=$2
COMPONENT_DIR="$TARGET_DIR/$COMPONENT_NAME"

# Create the component directory
mkdir -p "$COMPONENT_DIR"

# Create the JSX file
echo "import \"./$COMPONENT_NAME.css\";
import {Container} from \"@mui/material\";

const $COMPONENT_NAME = () => {
    return (
        <Container maxWidth=\"\">
            $COMPONENT_NAME Component
        </Container>
    );
};

export default $COMPONENT_NAME;" > "$COMPONENT_DIR/$COMPONENT_NAME.jsx"

# Create the CSS file
echo "" > "$COMPONENT_DIR/$COMPONENT_NAME.css"

echo "✅ Component '$COMPONENT_NAME' created successfully in '$COMPONENT_DIR'"
