-- Insert blog posts with full HTML content
-- Posts are ordered newest to oldest by created_at

INSERT INTO posts (post_id, slug, title, content, created_at) VALUES
(
    'a1b2c3d4-e5f6-7890-abcd-ef1234567890',
    'domain-object-validation',
    'Domain Object Validation - Jakarta Validation Annotations in the domain vs. Clean Architecture',
    '<p>
      As the list of prompts below shows, I made an initial decision to use Jakarta Validation annotations in the domain
      and let the boundaries invoke the validations using an ''Valid'' annotation.  It was a novel approach suggested by
      Claude, I thought why not. I was curious how it would look and was ready to go with it.
    </p>
    <p>It helps to talk to humans at lunch to get other opinions!  On the way home, I thought about Chpater 32 of the "Clean Architecture" book
      from Robert Martin.  I had been kidding myself into thinking that writing a
      <a href="https://github.com/bensimpson-ch/javablog.com/blob/main/services/domain/src/main/java/com/javablog/domain/Guard.java" target="_blank">Guard.against(...)</a>
      class to enforce my validations was too much effort, and that it would be more efficient to just use Jakarta
      annotations to validate my domain records.
    </p>
    <p>
      So, in one big commit, you can see that the domain is now independent of external frameworks. As this post involved a lot
      of prompting, I will end it here. No new shiny features, but a step forward.
    </p>

    <h2>Prompts Used (<a href="assets/transcripts/claude/january-30-2026.txt" target="_blank">full transcript</a>):</h2>
    <ul class="prompts">
      <li>"we are going to work on a blog post and the most important part of the application - our domain. Before we get started, lets define in the claude.md how our domain objects. these records require validation annotations like @NotNull of @NotEmpty depending upon the business rules. For instance a BlogPost must have content so the BlogPost must have a @NotEmpty String content element. where would this validation take place that the parameter provided is not null or empty?"</li>
      <li>[Selected: Application service layer - Services use @Validated class annotation. Validation happens regardless of which adapter calls the service.]</li>
      <li>"great - this is a new approach for me - typically I validate in the domain using a contraintsValidator.validate(this) in the domain record itself. I''m excited to validating at the application/domain service level will play out. write the blog post and comment domain objects using annotations that align with the already provided database limitations. for instance, title is not empty and maximum size 255 characters. Use the philosophy that everything is an object - Title has its own record that is used in the Post. all live in the blog package. Any questions? Perhaps its best to just git it a try and we tweak it afterwards. Each domain object requires a UnitTest. We do not use Mockito in our domain. The unit test for a domain object should always have a validateConstraints() test. If there are multiple parameters in a record constructor, then validateConstraints should be ParameterizedTest and the arguments should be fed from a static validateConstraints function in the same test."</li>
      <li>"pretty good - under no circumstances should the primary validateConstraints test have a method source that isn''t validateConstraints..."</li>
      <li>"null and empty values should not be added to the arguments tested in validateConstraints(). Better to use org.junit.jupiter.params.provider.NullAndEmptySource"</li>
      <li>"In the test code: com.javablog.domain define a class Fixture.java..."</li>
      <li>"ok, that worked. next topic - we want to no longer inherit from the spring-boot-starter-parent..."</li>
      <li>"great that worked fine. now lets introduce the maven-dependency-plugin:3.9.0..."</li>
      <li>"actually - make failOnWarning false and run a maven build to capture all of the warnings..."</li>
      <li>"remove warnings.txt and build-output.txt. update the tests to use the Fixture instead of constructing the records individually."</li>
      <li>"I''ve changed my mind after talking to a colleague today and do not want to try a variant of the same solution I''ve been using. lets keep the unit tests, but change the ways the validation is performed. Remove all annotations from the domain records."</li>
      <li>"I did not say to delete the validation tests - just the annotations. leave the tests. stop the procedure now."</li>
      <li>"alright - the domain is going to use an Uncle Bob classic ''Guard'' class in the com.javablog.domain package. This guard class has a static function: Guard.againstNull(Object value, String fieldName); Instead of using an @NotNull annotation the compact constructor should be defined without parameters and refer the value, for instance in Title, to the Guard.againstNull(value, ''Title.value'') implementation. If the object in againstNull is null - it should throw a ConstraintViolationException, a domain defined class that extends RuntimeException and prints a pretty message based upon the ''Title.value'' helper and the context of the throwing function. write the Guard, apply it to Title, update the test to use assertj to assert the exception is thrown with the appropriate exception instance and message."</li>
      <li>"its too early to execute the maven, the other tests need to be cleaned out first. change the other domain objects to use the Guard where appropriate. Add an additional Guard.againstEmpty. And Guard.againstInvalidLength. that should encompass all of the validations we were using. Update the domain classes to use these guards where we were using Annotations before. Remove the static of() and other static constructors I didn''t ask for."</li>
      <li>"remove the rest of the static of methods from the rest of the codebase."</li>
      <li>"haha - have a look at JpaBlogRepository"</li>
      <li>"Instead Of the BlogRepository.listPosts() returning a list of posts it should return a Posts object which takes a Set&lt;Post&gt; values in its constructor. The set may be empty, but not null."</li>
      <li>"apply the same pattern to Comments. use a ''plural'' domain object and its not allowed to have null values."</li>
      <li>"write unit tests following our conventions for Comment, Comments, Author, Post, Posts"</li>
      <li>"In Guard.java - there should be no value value != null - reuse the againstNull. Introduce a simplified againstMaxLength(String value, String fieldName, int maxLength)"</li>
      <li>"remove it. also change the name to match the convention instead of againstMaxLength use againstInvalidMaxLength"</li>
      <li>"ok, update the list of prompts we used in this session in home.html. I know it will be long, but we have a plan. Also update the domain claude.md to reflect the validation rules and how tests are written."</li>
      <li>"one last prompt.  in the home.html create a css class for the prompts - light grey.  Create a horizontal divider in between the posts - like an hr but it could be a border bottom."</li>
    </ul>',
    '2026-01-30 00:00:00'
),
(
    'b2c3d4e5-f6a7-8901-bcde-f12345678901',
    'flyway-spring-boot-4-cutoff',
    'Implementing the hexagonal architecture, first pain point, Claude''s cutoff date',
    '<p>AI-assisted coding is sometimes two steps forward, one step back. This post is about recognizing where LLM
      knowledge cutoff dates and a new major version collide. In an earlier post, I insisted on using Spring Boot 4+
      instead of the suggested version 3.4.2. Claude''s knowledge cutoff is May 2025, but
      <a href="https://spring.io/blog/2025/11/20/spring-boot-4-0-0-available-now" target="_blank">Spring Boot 4.0.0
        wasn''t released until November 20, 2025</a>—meaning the model had never seen it.
    </p>
    <p>Version 4 introduced several breaking changes from earlier Spring Boot versions. One specific example:
      configuring Flyway for database migrations. This post shows the correct approach. Only these three imports are
      required (from GitHub: <a href="https://github.com/bensimpson-ch/javablog.com/blob/main/services/adapter-persistence/pom.xml#L27-L38" target="_blank">adapter-persistence/pom.xml</a>):
    </p>

    <pre><code>
      &lt;dependency&gt;
        &lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;
        &lt;artifactId&gt;spring-boot-flyway&lt;/artifactId&gt;
      &lt;/dependency&gt;
      &lt;dependency&gt;
        &lt;groupId&gt;org.flywaydb&lt;/groupId&gt;
        &lt;artifactId&gt;flyway-database-postgresql&lt;/artifactId&gt;
      &lt;/dependency&gt;
      &lt;dependency&gt;
        &lt;groupId&gt;org.postgresql&lt;/groupId&gt;
        &lt;artifactId&gt;postgresql&lt;/artifactId&gt;
      &lt;/dependency&gt;
    </code></pre>

    <p>Add the migration scripts with the Flyaway naming conventions to the default location and you are ready:</p>
    <pre><code><a href="https://github.com/bensimpson-ch/javablog.com/tree/main/services/adapter-persistence/src/main/resources/db/migration"
    target="_blank">services/adapter-persistence/src/main/resources/db/migration</a></code></pre>

    <p>No additional configuration is needed in <a href="https://github.com/bensimpson-ch/javablog.com/blob/main/services/bootstrap/src/main/resources/application.properties"
    target="_blank">application.properties</a>. Flyway auto-configuration handles it
      by default.</p>

    <h2>The Lesson</h2>

    <p>AI coding assistants are trained on content before a cutoff date. When a software framework like Spring Boot
      introduces breaking changes, the coding assistant''s applies old (pre-cutoff date) solutions that don''t work.
      The "collaboration" session configuring persistence in my Spring Boot application was a frustrating waste of time
      and tokens. When something this fundamental isn''t working and the assistant keeps suggesting another "quick fix"...
      Stop. RTFM. The answer was in the
      <a href="https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide"
         target="_blank">Spring Boot 4.0 Migration Guide</a>, ready for any human alive after May 2025 to read.</p>

    <h2>Prompts Used (<a href="assets/transcripts/claude/january-28-2026.txt" target="_blank">full transcript</a>):</h2>
    <ul class="prompts">
      <li>"we are going to start living the hexagonal architecture as well as domain driven design. The domain should contain the objects that follow the principles of domain driven design. In the package com.javablog.domain.blog create the following records: Post(), Comment(). Create a BlogRepository to listPosts() and listComments(Post). Implement the repository in the adapter-persistence in the package com.javablog.adapter.persistence.blog. Note, use named queries that are anchored in the Entity object that lives in the adapter-persistence. Create a PostEntity and CommentEntity in the same package as the BlogRepository. Each entity will have UUID as its identifier. Each entity will have a content member that contains text or varchar with the maximum allowed. Do not use Clobs, Blobs - we will get to that later."</li>
      <li>[Multiple iterations of Claude adding flyway-core, configuration beans, and property changes—none worked]</li>
      <li>"no, you''re hacking - admit you don''t know what you are doing"</li>
      <li>"delete your hack. wait for further instruction."</li>
      <li>"well, I fixed it by reading the documentation. you see in spring 4+ the old paradigm on which you were trained was changed. note the changes and create a skeleton blog post 5 based upon it."</li>
      <li>"I will post this entry tomorrow. In the future, do not write the content for me - that too is wrong."</li>
      <li>"no, you''ve done enough today"</li>
      <li>"what is your knowledge cutoff date"</li>
    </ul>',
    '2026-01-29 00:00:00'
),
(
    'c3d4e5f6-a7b8-9012-cdef-123456789012',
    'hexagonal-architecture-springboot',
    'Hexagonal Architecture with Spring Boot, Flyway, and Dual Database Support',
    '<p>There is an addictiveness and joy that comes with visibly getting something done. Our work as software engineers
      is sometimes not visible to the end user for a long time. Delayed gratification. We press on anyway, trusting the
      architectural and design principles that enable us to adapt to changing requirements, whether a new feature or a
      non-functional requirement like a database change.</p>
    <p>AI-assisted coding shifts this paradigm. The reward comes faster. Its tempting to ignore everything you''ve
      learned to get the rush found in a completed feature. Building "shiny" new features feels great, but if they are
      built without a solid architecture, changing or extending them becomes difficult. A disciplined engineer needs to
      recognize this temptation and stick to the plan of developing quality software using a repeatable methodology. If
      working with a coding assistant, it''s still your job to tell Claude to use a specific architecture or pattern.</p>
    <p>This post shows how to restructure the basic starter Spring Boot application into a hexagonal architecture.
      We want an architecture that makes it easier to be flexible later. For example, this helps when changing databases
      from Postgres to MySQL or swapping Spring Boot for Quarkus.</p>
    <p>I''ve shared the prompts below along with two screenshots of how I work with IntelliJ and Maven. Questions or
    feedback? You''ll have to wait a few posts until my database is ready to save your comment.</p>

    <h2>Screenshots</h2>
    <figure class="thumbnails">
      <img src="assets/images/intellij-wide-view.png" alt="IntelliJ Wide View" class="lightbox-trigger" data-src="assets/images/intellij-wide-view.png">
      <img src="assets/images/claude-code-terminal-view.png" alt="Claude Code Terminal View" class="lightbox-trigger" data-src="assets/images/claude-code-terminal-view.png">
      <figcaption>Click to enlarge</figcaption>
    </figure>

    <h2>Prompts Used</h2>
    <ul class="prompts">
      <li>"we are starting on post 4. this post will take our services application to the next level. Convert this project to a multi-module hexagonal architecture with five modules: domain, application, adapter-rest, adapter-persistence, and bootstrap. Domain should have zero Spring dependencies. Application depends on domain with minimal Spring for Service annotation. Adapter-rest depends on application and domain. Adapter-persistence depends on application and domain. Bootstrap depends on all four, contains the SpringBootApplication class, application.properties, and spring-boot-maven-plugin. Add spring-boot-devtools as runtime optional in bootstrap for hot reload. Configure spring-boot-maven-plugin in bootstrap with jvmArguments for JDWP debug on port 5005. The application must start successfully with mvn clean install && mvn -pl bootstrap spring-boot:run."</li>
      <li>"these changes should break the github action to deploy the services application. review the Dockerfile and api-deploy.yml to verify that the application will deploy given its new architecture."</li>
      <li>"commit this and push this to test the github action again."</li>
      <li>"Configure dual database support. Keep H2 for integration tests using a test profile. Add a local profile for PostgreSQL that reads JAVABLOG_DB_USER, JAVABLOG_DB_PASS, and JAVABLOG_DB_NAME from environment variables, defaulting host to localhost:5432. Add the PostgreSQL driver dependency. Add a docker-compose.yml that spins up PostgreSQL with those environment variables. Update the README with instructions for running locally using either an existing PostgreSQL instance, docker-compose, or a podman run command."</li>
      <li>"There appears to be a missing configuration for production - it appears its your intention to start with a ''local'' profile in production - this means the api-deploy.yml has to receive this configurative step as well. I don''t like this. the application.properties should contain the production settings, the api-deploy.yml should not be modified."</li>
      <li>"final step, we are going to introduce jpa and flyaway in our adapter-persistence. Do not add entities yet. Create an adapter-persistence/claude.md to use the following persistence guidelines: My preference is to never use vendor specific annotations or query languages - whatever we use to implement the JPA spec. Follow these database naming standards: Use snake_case for all table and column names. Table names should be plural. Primary key columns should be spelled out as tablename_id not just id. Foreign key columns should match the primary key column name they reference. Avoid ambiguous column names. Apply these conventions to all JPA entities and any database migrations."</li>
      <li>"ok, that is enough for this post. create another blog post. This one will include a screenshot or two. The post should be structured Title: Post 4, Body: Post: 4 Screenshots: Sample Image I will delete Prompts: everything from this session including this one. I will commit and push after I test locally and update the content."</li>
    </ul>',
    '2026-01-28 00:00:00'
),
(
    'd4e5f6a7-b8c9-0123-def0-234567890123',
    'springboot-docker-with-claude',
    'Claude Prompts to create and deploy Dockerized Springboot Backend',
    '<p>This post will show the steps I took to generate a Spring Boot REST API for this javablog.  The API consists of a
    "hello javablog" RESTful get.  That''s enough to involve a lot of moving parts including (maven, Spring Boot,
    GitHub Actions and secrets, nginx reverse proxy, Cloudflare ssl/dns, ssh keys).  To my knowledge (cutoff point now),
      there is no way that a coding agent can do all of this yet.  I assume that MCPs will come to take away some of this
      pain in the future.  Perhaps instead of MCPs the coding agent will use its own DNS/SSL setup.  That will play out in the near future - watch out Cloudflare!</p>
    <p>
      As you can see in this example, the first draft from Claude was a bust.  I had to delete it and then use the Spring
      Website: Spring Initializr to create an example of what I wanted.  I pasted the generated pom.xml into the Claude
      window to show exactly what I wanted (another MCP opportunity).  This worked fine and my "hello javablog" was born.
    </p>
    <p>
      The failed first attempt delivered the wrong major version of Spring Boot despite explicitly asking for it.
      It wasn''t until I deleted the generated code and asked for it with an example that I got what I wanted.  Of course
      this is far from done but insisting upon two kinds of tests (unit test and integration test) means that the desired
      behavior is repeatable.
    </p>
    <p>
      The non-Claude activities in this example focus on my Cloudflare account and my Ubuntu API Server.  I have
      firewalled Claude out of these areas because a mistake could cause significant time loss and/or security breaches.
      I''ve listed the activities below in case you want to try to follow along.
    </p>
    <p><b><a href="https://github.com/bensimpson-ch/javablog.com/commits/main/?since=2026-01-27&until=2026-01-27" target="_blank">Github Commits</a></b></p>
    <p><b>Activities and Assumptions:</b></p>
    <ul>
      <li>you have root access to an Ubuntu server with more than 4GB RAM, 1 CPU and 20GB Disk.</li>
      <li>your Ubuntu server has docker and nginx installed</li>
      <li>your DNS and SSL are managed by Cloudflare or a developer-oriented provider</li>
      <li>follow this nginx example on your server:
        <a href="https://blackist.medium.com/fast-setup-nginx-https-reverse-proxy-for-springboot-api-32f9ca70f97">
          Leon Dong - Medium - Fast-Setup: Nginx Https Reverse Proxy for Springboot API</a></li>
      <li>configure an ''A'' record pointing api.yourdomain.com to your IP address.</li>
      <li>configure "full" SSL between the client, cloudflare, and your nginx server.  This means you will need to
        generate a private key and certificate here: <br/> Cloudflare -> Select Domain -> SSL/TLS ->Client Certificates</li>
    </ul>
    <h2>API Demo</h2>
    <p>Response from <code>https://api.javablog.com/hello</code>:</p>
    <pre>javablog</pre>

    <h2>Prompts Used</h2>
    <ul class="prompts">
      <li>"we are going to setup a rest services application to eventually permit comments and feedback for blog posts.
        As this service will need a database and other services, let''s assume it will run in a docker image running
        inside a docker-compose constructed network.  this project will require a sonar and nexus connection -
        both are already in my maven settings.  Use spring boot and rest, jpa, security.
        version 4.0.2.  write a simple hello world "hello javablog"  and have the complete maven build test the hello
        world."</li>
      <li>"this isn''t what I asked for.  I specifically asked for rest, jpa and security.  You gave me web mvc, jdbc
        drivers and a load of comments"</li>
      <li>"this is not what I asked for.  Have a look at the generated file I got by using the spring initialzr.
        this is what i expected and asked for.  latest 4.0.2 parent starter, rest, jpa, security and NO MVC.
        [Pasted text #2 +88 lines]"</li>
      <li>"we are going to try again - I''ve deleted your first draft.  I want a spring boot REST, JPA, Security
        application in a single jar, using maven and spring boot 4.0.2.  The application should import from spring
        boot starter parent 4.0.2. there should be a hello "javablog" rest service with a functional unit test using
        junit jupiter 5+ and a spring boot specific integration test.  put this project in the
        services directory. "</li>
      <li>"alright - my blogs backend needs to be dockerized - the angular app is delivered via github action to MS Azure.
        The backend needs to be dockerized not just locally, but the container must be built via a github action that
        builds the image and makes it available to a server that will use the docker image. For now we will ignore the
        database and security complexity of hello world.  The backend will be
        served via a nginx proxy at api.javablog.com."</li>
      <li>"I have podman running locally"</li>
      <li>"Ok, I''ve reviewed the api-deploy.yml - it''s incomplete.  you do not know the server name where we will deploy
        the backend to yet.  why don''t we store this as a variable with a public key so the action can communicate with
        the server - 161.97.175.172"</li>
      <li>"your instructions are not correct - copy and save as GHCR_TOKEN secret - where? "</li>
      <li>"aha, now you are giving accurate instructions."</li>
      <li>"I want the SERVER_HOST and SERVER_USER to also be variables since the script is visible to the world."</li>
      <li>"done, I added the correct non-root user and the host."</li>
      <li>"while I did everything you asked, the script fails to deploy. [error log from GitHub Actions showing ssh:
        no key found]"</li>
      <li>"there is no passphrase"</li>
      <li>"the problem was the spaces in the key. When I put the key in the api-deploy.yml incorrect spacing was added.
        the hello world is visible on my server now."</li>
      <li>"we are not done yet. I updated the dns and ssl settings and the hello javablog is available via
        https://api.javablog.com/hello using the ''full'' ssl option and an nginx proxy with the certificates provided by
        Cloudflare. Lets hook the blog up to show hello - then response. For this we need to start a new post. As before,
        the post should be top most in the list. We are not concerned with anything other than following the previous
        patterns at this point. Create a post titled Post 3, with placeholder body Post Body 3, and the demo calling the
        hello world, then all of the prompts used to get to this point."</li>
    </ul>',
    '2026-01-27 00:00:00'
),
(
    'e5f6a7b8-c9d0-1234-ef01-345678901234',
    'seo-with-claude',
    'Setting Up SEO with Claude, first steps',
    '<p>I write this post on a train somewhere between Winterthur and the Airport. It''s a quiet Sunday afternoon train
      without tourists or children - including my little angels. Working on the train has an advantage that it''s generally
      a fixed block of time to get something done. It''s literally a physical and mental "context switch" when you arrive
      at your destination. This switch is the pressure to complete the task.  So, about that JavaBlog.com post, let''s get started.</p>

    <p>I thought before I go too far developing this blog that it would be useful to develop an initial strategy for how
      I''d approach SEO with JavaBlog.com. I''ve done this in the past, using the early rules of meta tags and page titles.
      The rules have obviously changed, but the fundamentals haven''t - content is king.</p>

    <p>For those unfamiliar: SEO (Search Engine Optimization) is how you make your content discoverable. One foundational
      step is creating a <strong>sitemap.xml</strong> - essentially a table of contents for search engines that lists
      every page on your site and when it was last updated. Without one, search engines have to guess what exists.</p>

    <p>I plan to promote my other endeavors using JavaBlog.com, including an application called Polybit.ch. But first,
      I need to master what I call "vibing" - using conversational prompts with AI coding assistants like Claude to
      build software iteratively. Think of it as pair programming where your partner has read every programming book
      ever written but needs you to drive the vision.</p>

    <p>This blog has value to me on both a personal and financial level. I purchased this domain in 2002 - I love
      coffee and coding so I thought it was a perfect match. I was 29, fresh from studying foreign languages and math.
      A fantastic Russian professor had encouraged me to find something to talk about with people who speak my languages.
      Fast forward 24 years and roughly $500 in domain renewals - I''m ready, again, for the sixth time.</p>

    <h2>What I Did Today</h2>

    <p>I used Claude to generate a sitemap.xml based on my existing page structure. More importantly, I configured the
      project rules (in a <code>claude.md</code> file) so that future pages are automatically added to the sitemap
      when created. This "set it and forget it" automation is where AI-assisted coding really pays off.</p>

    <p>At the end of this session, I submitted my sitemap.xml to the
      <a href="https://search.google.com/search-console">Google Search Console</a> and will wait a few days until
      this content is indexed. It''s the long game with SEO - content and "safe" best practices. Cheers.</p>

    <h2>Prompts Used</h2>
    <ul class="prompts">
      <li>"the next few prompts will be public, including this one. I want to improve the SEO of my blog so that others can find my posts and be inspired to also experiment with ''vibing'' or ''LLM assisted coding''. The task today is to improve the SEO for the website https://javablog.com. The website has a single path that is currently visible - ''/''. Create a sitemap.xml that I can submit to the google search console. Also suggest an approach to SEO that: 1. works with translated content. 2. works in a single PR. 3. Can be extended later when there is more content."</li>
      <li>"lets define a favicon.ico file that shows a capital ''J'' in a darker purple with a background color white."</li>
      <li>"I have convert installed, you can run that command."</li>
      <li>"Alright - start a blog post in the home component above the previous post. chronologically, the newest post will be at the top. Title: Setting Up SEO with Claude, first steps. In the body do not write anything just write ''body here''. After the body, leave an area for ''Prompts used''."</li>
      <li>"I would like you to include my rule about adding new pages to sitemap.xml to my claude.md. In the future, when I make changes to the site structure, automatically update my sitemap.xml file."</li>
      <li>"Great. Update my prompts to include the history of this ''vibe'' session. Good job. Commit it in our names. I will review the commits for any unexpected changes."</li>
    </ul>',
    '2026-01-18 00:00:00'
),
(
    'f6a7b8c9-d0e1-2345-f012-456789012345',
    'building-javablog',
    'How I Built This Blog with Claude AI (And the Exact Prompts I Used)',
    '<p>
      Coding with Claude has become so fun and interesting that I''ve decided to share these adventures
      on my javablog.com. I''ve been "vibe" coding on a weekly basis for the past 6 months on personal projects. At
      work I use github co-pilot selectively to generate tests, translate from English to German or for technical
      documentation like Plant UML: sequence and class diagrams. I''ve tried Gemini''s CLI - it did well but
      Claude is better so far.
    </p>
    <p>
      I generally avoid letting claude run my maven build, npm or ng tools. I''ve seen that my token allocation would dry
      up without much to show for it. I set the clear boundary that Claude codes, I test and build. By coding, the
      coding
      agent is not writing without me specifying what I want. The specification is generally a series of short questions
      about trade-offs, best practices and then I have a minimal specification for the next step.</p>
    <p>So, in this project I''ve gone out of my comfort zone and let the coding agent just build it with a minimum amount
      of inputs. I will share my prompts at the end - and the github repository should be publicly visible:
      <a href="https://github.com/bensimpson-ch/javablog.com">Github - Javablog.com</a></p>
    <h2>The Stack</h2>
    <p>
      JavaBlog.com runs on the following stack:
    </p>
    <ul>
      <li><strong>Angular 20</strong> with static site generation (SSG)</li>
      <li><strong>Azure Static Web Apps</strong> for hosting</li>
      <li><strong>Cloudflare</strong> for DNS and CDN</li>
      <li><strong>GitHub Actions</strong> for CI/CD</li>
    </ul>
    <h3>My Prompts</h3>
    <ul class="prompts">
      <li>"you now should have access to javablog.com... I want to use it to make posts. I think given your talents - we will not use a blogging tool, instead we will just create content. create a claude.md for you to follow"</li>
      <li>"You took it too far. I want an angular application app to live in an app directory. when the github action triggers, it should build the app and deploy it to azure."</li>
      <li>"I want it built from day one to be SEO and AI friendly."</li>
      <li>"use the latest version of angular - its already installed."</li>
      <li>"create a standard gitignore for an angular and claude project. i''ve already created the file in javablog.com"</li>
      <li>"we need to setup a protocol for you preparing a post for me. It will use my name so I have the final say on content, etc. The first post will be about the blog itself, its structure and our collaboration. first things first, the layout. I want a simple layout. JavaBlog.com is the title of the blog top right. There will only be blog entries which will be listed on the index.html."</li>
      <li>"create a post to show how the first post will look. make it about setting up this blog together with you."</li>
      <li>"This isn''t what I specified. Do not abbreviate the first post. It should live at javablog.com/ as well as its seo path."</li>
      <li>"Stick to just the prompts used to build the blogging website. list the prompts in the ul element of home.html including this one."</li>
    </ul>',
    '2025-12-22 00:00:00'
);
