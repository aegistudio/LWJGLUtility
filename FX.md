================================
* Thinking in TransparentGL FX *
================================

1. Design Pattern
-----------------

	FX, or effect, in TransparentGL is refer to the rendering effects provided by shaders. The TransparentGL FX provides mechanisms about assembling shaders, or "blending" rendering effect of different shaders.

	Blending rendering effects is always neccessary. In fixed-pipe OpenGL, we're always blending the effect of lighting, fog, and multi-sampling. When using shaders, we still have the requirement of blending effects. For instance, when rendering a vase beside a window, we need to render the shadow blocked by the vase using ray-tracing, the grooves on the vase using normal map, and sometimes anti-aliasing. Each one of the shading effects may correspond to a function in GLSL.
	
	So is there any way to simplify the development of shaders. Is it possible to write a shader of specific function once, and blend them according to the rendering requirement? I tried to provide a solution to this problem, though it my be not the best way.
	
	The following problems are to be solved:
	- How to organize a single shader, or a component shader?
	- How to determine the order of executing the shaders?
	- Considering the hierarchical structure of TransparentGL rendering, when a group of shaders is enabled on the upper level of the rendering tree (The root on the top), what will happen when another group on the lower level of the tree is enabled?
	- Some shader need pre-rendering in order to render, How to provide pre-rendering service?
	
2. Shader Organizing
--------------------

	I create the SHML(shader markup language) to describe a single or a component shader. A SHML is a XML(extensive markup language), just like HTML(hypertext markup language).

	SHML describes a single/component shader program (we use shader program to function in GLSL, rather than a single shader), which comprises vertex shader(s), fragment shader(s), GL30 geometry shader(s), tessellation shader(s), and so on. When composing a single/component SHML, the user just need to write it as if there's no other shaders in use, which means the user just need to consider a single effect at a time. "Considering one effect at a time" is a important concept, especially when you'd like to write a universal shader.
	
	SHML is actually a file-system level shader, there's also a runtime level class corresponds to the SHML, the Effect class. There's (always) one stored rendering program on the disk, but there're could be multiple Effect instances using this program. It's useful when you write a program like per-pixel lighting, what will happen if there's only one such lighting program permitted be run in your game? The concept is analogous to the relation between a "executable(.exe) file" residing on your disk and processes of such .exe file running in your memory.

	But allowing such program to used multiple times also brings terrible problems. The most acute problem is the naming conflict. We know that namespacing is not allowed in GLSL, so all Effects of such program are using the same namespace, it will conflict if we don't do anything to distinguish it. Temporarily we give all shader-scoped functions and variables aliased identifiers. But in the next version, I'm not sure whether I will just alias shader-scoped variables and main, while functions are shared between all shaders. Or two of these mechanisms will be used. Whatever mechanism I'll use, the usage of shader blending is still transparent to users.
	
3. Shader Blending
------------------

	Now it's the biggest problem in our FX module, how could we blend these shaders written in SHML together? In shader blending, I've implement the strip and plug mechanism.

	The shader strip is the execution unit of rendering. All shader effects are attached to the shader strip through some other policy. The plug iimplements the policy. It arranges the effects inside it and could be arranged by the shader strip.

	The shader strip is a "strip" because it executes the shaders inside the strip sequentially. For convenience, we assign a value to a plug to determine the execution order of the plug. And the value is called the "priority" of the plug. (We don't directly take the order of adding plug as the order of execution, because it will cause contradiction when absorbing a sub-level shader strip into the super-level one.)

	Besides priority, the plug also has the right to determine the count of shaders to be plugged in and what kinds of shaders could be plugged. On the problem of multiplicity, some shader plugs are exclusive, which means only one shader could be plugged in. Multiple shaders plugged will cause unexcepted result (You won't except two anti-aliasing algorithms to be used in the same time). If another shader (can be plugged) is added, the original one will be abandoned. Other plugs allow multiple shaders to be plugged, like the lighting effects. If another shader is added, it will be executed after the original one. On the problem of classification, you won't wish an anti-alising effect to be plugged in the lighting plug, or lighting one in the anti-alising effect.

	The detailed mechanism of shader blending takes me nearly one month to figure out. On the plan of arranging shaders, I once adopt the simple three-phase strip and the programable plug. The three-phase strip has preliminary, normal and postmorten phase. The preliminary phase execute first, then normal and finally the postmorten. The schema is simple to realize but the main shortage of this schema is lacking flexibility of arranging effects. The programmable plug consists of multiple plugs in a shader organization (not a strip structure), "programmable" means the user could customize the execution of plugs. Programmable plug has the complete flexibility and the customizable policy. But this schema increases the using complexity. It also brings great dilemma when blending super-level and sub-level shader organization together. As a trade off, I use the strip to arrange plugs, and the plug to use concrete policy.

4. Shade 