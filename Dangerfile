# github comment settings
github.dismiss_out_of_range_messages

# Warn when there are merge commits in the diff
if git.commits.any? { |c| c.message =~ /^Merge branch 'rxanko'/ }
    warn 'Please rebase to get rid of the merge commits in this PR'
end

checkstyle_format.base_path = Dir.pwd

# detekt
checkstyle_format.report 'app/build/reports/detekt/detekt-checkstyle.xml'

# # Android Lint
require 'android_lint_translate_checkstyle_format'
android_lint_xml = ::AndroidLintTranslateCheckstyleFormat::Script.translate(File.read('app/build/reports/lint-results.xml'))
checkstyle_format.report_by_text android_lint_xml

failures = Array.new
testsCount = 0
Dir.glob('app/build/test-results/testDebugUnitTest/*.xml') do |rb_file|
    junit.parse rb_file
    failures = (failures + junit.failures) unless junit.failures.empty?
    testsCount = (testsCount + junit.tests.size) unless junit.tests.empty?
end

unless failures.empty?
    fail("Tests have failed, see below for more information. (#{failures.size}/#{testsCount})")
else
    message("All test passed. (#{testsCount}/#{testsCount})")
end

message = "Test case | Classname|\n"
message << "--- | --- |\n"
failures.each do |test|
    message << test.attributes[:name] +" | "+ test.attributes[:classname] + "|\n"
end
markdown message unless failures.empty?