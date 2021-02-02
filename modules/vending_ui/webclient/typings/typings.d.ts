//To use non code assets with typescript we need to defer the type for these imports.
//This requires a custom.d.ts file which signifies the custom definitions for typescript in our projects
//We declared a new module for SVGs by specifying any import that ends in .svg and defining the module's
//content as any. We could be more explicit about it being a url by defining the type as string.
declare module "*.svg" {
  const content: any;
  export default content;
}

//The above concept applies for other assets including css, scss, json and more.